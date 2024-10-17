import Authentication from "./components/pages/auth/Auth";
import Home from "./components/pages/home/Home";
import "./index.css";
import { AuthProvider } from "./context/AuthProvider";
import { Route, Routes } from "react-router-dom";

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Authentication mode="login" />} />
        <Route path="/register" element={<Authentication mode="register" />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
