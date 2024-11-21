import React, { createContext, useMemo, useCallback, useContext } from 'react';
import PropTypes from 'prop-types';

export const AuthContext = createContext();

const clearStorage = () => {
    localStorage.clear();
    sessionStorage.clear();
};

export const AuthProvider = ({ children }) => {

    const login = useCallback(async (data) => {
        if (data) {
            localStorage.setItem("user", JSON.stringify(data.user));
            sessionStorage.setItem("accessToken", data.accessToken);
            sessionStorage.setItem("refreshToken", data.refreshToken);
            // sessionStorage.setItem("expiresIn", data.expiresIn);
        }
    }, []);

    const logout = useCallback(() => {
        clearStorage();
    }, []);

    const getUser = useCallback(() => {
        const user = localStorage.getItem("user") || '';
        if (user) {
            return JSON.parse(user);
        }
    }, []);

    const getAccessToken = useCallback(() => {
        return sessionStorage.getItem("accessToken") || '';
    }, []);

    const getRefreshToken = useCallback(() => {
        return sessionStorage.getItem("refreshToken") || '';
    }, []);

    const isAuthenticated = useCallback(() => !!getAccessToken() && !!getUser(), [getAccessToken, getUser]);

    const contextValue = useMemo(() => ({
        login,
        logout,
        getUser,
        getAccessToken,
        getRefreshToken,
        isAuthenticated
    }), [login, logout, getUser, getAccessToken, getRefreshToken, isAuthenticated]);

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