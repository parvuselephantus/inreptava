import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import ClassList from './component/ClassList';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';


const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<ClassList />} />
        </Routes>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
