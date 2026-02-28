import { useState } from 'react'
import List from './components/List';

function App() {

  const [menu, setMenu] = useState("home");

  const myHeaders = {
    products: [
      { label: "ID", key: "id" },
      { label: "Code", key: "code" },
      { label: "Name", key: "name" },
      { label: "Price", key: "price" } 
    ],
    raw_materials: [
      { label: "ID", key: "id" },
      { label: "Code", key: "code" },
      { label: "Name", key: "name" },
      { label: "Stock", key: "quantityStock" }
    ],
    product_materials: [
      { label: "ID", key: "id" },
      { label: "Product", key: "productName" },
      { label: "Material", key: "rawMaterialName" },
      { label: "Required", key: "quantityRequired" }
    ]
  };

  function clickMenu(screen) {
    setMenu(screen);
  }

  return (
    <>
      <header>
        <ul>
          <li onClick={() => clickMenu("home")}>Home</li>
          <li onClick={() => clickMenu("products")}>Products</li>
          <li onClick={() => clickMenu("raw_materials")}>Raw Materials</li>
          <li onClick={() => clickMenu("product_materials")}>Links</li>
        </ul>
      </header>

      <div className='content'>
        <List screen={menu} headers={myHeaders} />
      </div>
    </>
  )
}

export default App;