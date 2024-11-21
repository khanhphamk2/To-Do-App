import Authentication from "./components/pages/auth/Auth";
import Home from "./components/pages/home/Home";
import "./index.css";
import { AuthProvider } from "./context/AuthProvider";
import { Route, Routes } from "react-router-dom";
import MyDay from "./components/pages/home/MyDay";

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Authentication mode="login" />} />
        <Route path="/register" element={<Authentication mode="register" />} />
        <Route path="/forgot-password" element={<Authentication mode="forgot-password" />} />
        <Route path="/reset-password" element={<Authentication mode="reset-password" />} />
        <Route path="/my-day" element={<MyDay />} />
        <Route path="/important" element={<Home />} />
        <Route path="/completed" element={<Home />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
