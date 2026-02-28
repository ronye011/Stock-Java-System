import { useState, useEffect } from "react";

function DynamicForm({ screen, onSubmit, onCancel, initialData }) {

  const formConfig = {
    products: [
      { name: "code", label: "Code", type: "text" },
      { name: "name", label: "Name", type: "text" },
      { name: "price", label: "Price", type: "number" },
    ],
    raw_materials: [
      { name: "code", label: "Code", type: "text" },
      { name: "name", label: "Name", type: "text" },
      { name: "quantityStock", label: "Quantity", type: "number" },
    ],
    product_materials: [
      { name: "productId", label: "Product ID", type: "number" },
      { name: "rawMaterialId", label: "Raw Material ID", type: "number" },
      { name: "quantityRequired", label: "Quantity Required", type: "number" },
    ],
  };

  const [formData, setFormData] = useState({});

  const config = formConfig[screen];
  if (!config) return null;

  useEffect(() => {
    if (initialData) {
      const formattedData = { ...initialData };
      if (screen === 'product_materials' && initialData.product) {
        formattedData.productId = initialData.product.id;
        formattedData.rawMaterialId = initialData.raw_material.id;
      }
      setFormData(formattedData);
    } else {
      const emptyForm = {};
      config.forEach(field => emptyForm[field.name] = "");
      setFormData(emptyForm);
    }
  }, [initialData, screen]);

  function handleChange(e) {
    const { name, value, type } = e.target;
    const finalValue = type === "number" ? Number(value) : value;
    
    setFormData(prev => ({
      ...prev,
      [name]: finalValue,
    }));
  }

  function handleSubmit(e) {
    e.preventDefault();
    
    if (initialData) {
      if (screen === 'product_materials') {
        onSubmit({ quantityRequired: formData.quantityRequired });
      } else {
        const changes = {};
        Object.keys(formData).forEach(key => {
          if (formData[key] !== initialData[key]) {
            changes[key] = formData[key];
          }
        });
        onSubmit(changes);
      }
    } else {
      onSubmit(formData);
    }
  }

  return (
    <>
      <div
        style={{
          position: "fixed",
          inset: 0,
          background: "rgba(0,0,0,0.5)",
          zIndex: 9999
        }}
        onClick={onCancel}
      ></div>

      <form
        style={{
          position: "fixed",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          background: "#fff",
          padding: "20px",
          zIndex: 10000,
          borderRadius: "8px",
          boxShadow: "0 4px 6px rgba(0,0,0,0.1)",
          width: "300px"
        }}
        onSubmit={handleSubmit}
      >
        <h3>{initialData ? "Edit" : "New"} {screen}</h3>

        {config.map(field => {
          const isIdField = field.name === "productId" || field.name === "rawMaterialId";
          const isEditing = !!initialData;
          const readOnly = screen === 'product_materials' && isEditing && isIdField;

          return (
            <div key={field.name} style={{ marginBottom: "10px" }}>
              <label style={{ display: "block", marginBottom: "5px" }}>{field.label}</label>
              <input
                type={field.type}
                name={field.name}
                value={formData[field.name] || ""}
                onChange={handleChange}
                readOnly={readOnly}
                style={{ 
                  width: "100%", 
                  padding: "8px",
                  backgroundColor: readOnly ? "#f0f0f0" : "white",
                  cursor: readOnly ? "not-allowed" : "text"
                }}
              />
            </div>
          );
        })}

        <div className="btn-group" style={{ marginTop: "15px", display: "flex", gap: "10px" }}>
          <button className="btn-success" type="submit" style={{ flex: 1, padding: "10px" }}>
            Save
          </button>
          <button
            className="btn-danger"
            type="button"
            onClick={onCancel}
            style={{ flex: 1, padding: "10px" }}
          >
            Cancel
          </button>
        </div>
      </form>
    </>
  );
}

export default DynamicForm;