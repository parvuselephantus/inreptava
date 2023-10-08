import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import ClassList from './component/ClassList';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<ClassList />} />
      </Routes>
  </BrowserRouter>
  );
}

export default App;
