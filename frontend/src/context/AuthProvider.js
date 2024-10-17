import React, { createContext, useState, useEffect, useMemo, useCallback, useContext } from 'react';
import { useNavigate } from "react-router-dom";
import { login } from "../api/auth.api";
import PropTypes from 'prop-types';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [token, setToken] = useState(localStorage.getItem('token') || null);
    const navigate = useNavigate();

    useEffect(() => {
        if (token) {
            localStorage.setItem('token', token);
        } else {
            localStorage.removeItem('token');
        }
    }, [token, user]);

    const handleLogin = useCallback(async (data) => {
        const response = await login(data);
        if (response.data) {
            setUser(response.data.user);
            setToken(response.data.token);
            localStorage.setItem("user", JSON.stringify(response.data.user));
            localStorage.setItem("token", response.data.token);
            navigate("/");
            return;
        }
        throw new Error("Login failed");
    }, [navigate]);

    const handleLogout = useCallback(() => {
        setUser(null);
        setToken("");
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        navigate("/");
    }, [navigate]);

    const contextValue = useMemo(() => ({ user, token, handleLogin, handleLogout }), [user, token, handleLogin, handleLogout]);

    return (
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>);
};

export const useAuth = () => {
    return useContext(AuthContext);
}

AuthProvider.propTypes = {
    children: PropTypes.node.isRequired,
};