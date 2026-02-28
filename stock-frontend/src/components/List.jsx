import { useState, useEffect } from "react";
import DynamicForm from "./Form";

function List({ screen, headers }) {
  const [showForm, setShowForm] = useState(false);
  const [data, setData] = useState([]);
  const [selectedItem, setSelectedItem] = useState(null);
  const [loading, setLoading] = useState(false);
  const [toast, setToast] = useState({ message: "", type: "" });
  
  const [productionData, setProductionData] = useState([]);

  const endpointMap = {
    products: "products",
    raw_materials: "raw-materials",
    product_materials: "product-materials",
  };

  const endpoint = endpointMap[screen];

  const showToast = (message, type) => {
    setToast({ message, type });
    setTimeout(() => setToast({ message: "", type: "" }), 4000);
  };

  const calculateProduction = async () => {
    setLoading(true);
    try {
      const [productsRes, materialsRes, structureRes] = await Promise.all([
        fetch(`http://localhost:8080/products`),
        fetch(`http://localhost:8080/raw-materials`),
        fetch(`http://localhost:8080/product-materials`)
      ]);

      const products = await productsRes.json();
      const rawMaterials = await materialsRes.json();
      const structures = await structureRes.json();

      const suggestions = products.map(product => {
        const neededMaterials = structures.filter(s => s.productId === product.id);
        
        if (neededMaterials.length === 0) {
          return null;
        }

        let maxCanProduce = Infinity;

        neededMaterials.forEach(needed => {
          const stockMaterial = rawMaterials.find(rm => rm.id === needed.rawMaterialId);
          
          if (!stockMaterial || stockMaterial.quantityStock < needed.quantityRequired) {
            maxCanProduce = 0;
          } else {
            const possible = Math.floor(stockMaterial.quantityStock / needed.quantityRequired);
            if (possible < maxCanProduce) {
              maxCanProduce = possible;
            }
          }
        });

        const finalQuantity = maxCanProduce === Infinity || maxCanProduce < 0 ? 0 : maxCanProduce;
        
        return {
          productId: product.id,
          productName: product.name,
          quantityToProduce: finalQuantity,
          unitValue: product.price,
          totalValue: finalQuantity * product.price
        };
      });

      const finalSuggestions = suggestions
        .filter(s => s !== null && s.quantityToProduce > 0)
        .sort((a, b) => b.totalValue - a.totalValue);

      setProductionData(finalSuggestions);
    } catch (error) {
      console.error("Erro ao calcular:", error);
      showToast("Error calculating production", "error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setSelectedItem(null);
    setData([]);
    setProductionData([]);

    if (screen === "home") {
      calculateProduction();
      return;
    }

    if (!endpoint) return;
    setLoading(true);
    fetch(`http://localhost:8080/${endpoint}`)
      .then((res) => res.json())
      .then((result) => setData(result || []))
      .catch(() => {
        setData([]);
        showToast("Error loading data", "error");
      })
      .finally(() => setLoading(false));
  }, [screen, endpoint]);

  function handleNew() {
    setSelectedItem(null);
    setShowForm(true);
  }

  function handleEdit() {
    if (!selectedItem) {
      alert("Select a record to edit");
      return;
    }
    setShowForm(true);
  }

  function handleDelete() {
    if (!selectedItem) {
      alert("Select a record to delete");
      return;
    }
    if (!window.confirm("Are you sure?")) return;

    fetch(`http://localhost:8080/${endpoint}/${selectedItem.id}`, {
      method: "DELETE",
    })
      .then((res) => {
        if (!res.ok) return res.text().then((text) => { throw new Error(text); });
        setData((prev) => prev.filter((item) => item.id !== selectedItem.id));
        setSelectedItem(null);
        showToast("Record deleted successfully", "success");
      })
      .catch((err) => showToast(err.message, "error"));
  }

  function handleSave(formData) {
    const method = selectedItem ? "PUT" : "POST";
    const url = selectedItem
      ? `http://localhost:8080/${endpoint}/${selectedItem.id}`
      : `http://localhost:8080/${endpoint}`;

    fetch(url, {
      method: method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    })
      .then((res) => {
        if (!res.ok) return res.text().then((text) => { throw new Error(text); });
        return res.json();
      })
      .then((result) => {
        if (selectedItem) {
          setData((prev) => prev.map((item) => (item.id === selectedItem.id ? result : item)));
          showToast("Record updated successfully", "success");
        } else {
          setData((prev) => [...prev, result]);
          showToast("Record created successfully", "success");
        }
        setShowForm(false);
        setSelectedItem(null);
      })
      .catch((err) => showToast(err.message, "error"));
  }

  if (screen === "home") {
    const totalValue = productionData.reduce((sum, item) => sum + item.totalValue, 0);

    return (
      <div>
        <h2>Production Ranking Dashboard</h2>
        <div>
          <h4>Total Estimated Value: <strong>${totalValue.toFixed(2)}</strong></h4>
          <p>Prioritized by highest value products.</p>
        </div>

        {loading && <p>Loading and calculating...</p>}
        
        {!loading && productionData.length === 0 && <p>No products can be produced with current stock.</p>}

        {!loading && productionData.length > 0 && (
          <table border="1" style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr>
                <th style={{ padding: "10px" }}>Rank</th>
                <th style={{ padding: "10px" }}>Product Name</th>
                <th style={{ padding: "10px" }}>Quantity to Produce</th>
                <th style={{ padding: "10px" }}>Unit Value</th>
                <th style={{ padding: "10px" }}>Total Value</th>
              </tr>
            </thead>
            <tbody>
              {productionData.map((item, index) => (
                <tr key={item.productId}>
                  <td style={{ padding: "10px", fontWeight: index === 0 ? "bold" : "normal" }}>
                    {index + 1}°
                  </td>
                  <td style={{ padding: "10px" }}>{item.productName}</td>
                  <td style={{ padding: "10px" }}>{item.quantityToProduce}</td>
                  <td style={{ padding: "10px" }}>${item.unitValue.toFixed(2)}</td>
                  <td style={{ padding: "10px", fontWeight: "bold" }}>${item.totalValue.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    );
  }

  const currentHeaders = headers[screen];
  if (!currentHeaders) return <p>No data available</p>;

  return (
    <>
      {toast.message && (
        <div style={{
          position: "fixed", top: "20px", right: "20px", padding: "15px",
          background: toast.type === "success" ? "#d4edda" : "#f8d7da",
          color: toast.type === "success" ? "#155724" : "#721c24",
          border: `1px solid ${toast.type === "success" ? "#c3e6cb" : "#f5c6cb"}`,
          borderRadius: "5px", zIndex: 10001
        }}>
          {toast.message}
        </div>
      )}

      <div className="buttons-menu">
        <button className="btn-success" onClick={handleNew}>New</button>
        <button className="btn-primary" onClick={handleEdit}>Edit</button>
        <button className="btn-danger" onClick={handleDelete}>Delete</button>
      </div>

      {showForm && (
        <DynamicForm
          screen={screen}
          onSubmit={handleSave}
          onCancel={() => {
            setShowForm(false);
            setSelectedItem(null);
          }}
          initialData={selectedItem}
        />
      )}

      {loading && <p>Loading...</p>}

      <table border="1" style={{ width: "100%", borderCollapse: "collapse", marginTop: "10px" }}>
        <thead>
          <tr>
            {currentHeaders.map((header) => (
              <th key={header.key} style={{ padding: "10px" }}>{header.label}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.length === 0 ? (
            <tr>
              <td colSpan={currentHeaders.length} align="center" style={{ padding: "10px" }}>No data available</td>
            </tr>
          ) : (
            data.map((item) => (
              <tr
                key={item.id}
                onClick={() => setSelectedItem(item)}
                style={{
                  backgroundColor: selectedItem?.id === item.id ? "#d3d3d3" : "white",
                  cursor: "pointer",
                }}
              >
                {currentHeaders.map((header) => (
                  <td key={`${item.id}-${header.key}`} style={{ padding: "10px" }}>
                    {typeof item[header.key] === 'object' && item[header.key] !== null
                      ? item[header.key].name || item[header.key].id
                      : item[header.key]}
                  </td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </>
  );
}

export default List;