import { useState } from 'react'

import './App.css'
import { Route, Routes } from 'react-router-dom'
import EcommerceAdmin from './pages/EcommerceAdmin'

function App() {

  return (
    <>
      <Routes>
        <Route path='/*' element={<EcommerceAdmin />} />
      </Routes>
    </>
  )
}

export default App
