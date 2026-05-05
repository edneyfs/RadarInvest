import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Dashboard } from './pages/Dashboard';
import { AdminPage } from './pages/AdminPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import { authService } from './services/authService';
import AdminLoginPage from './pages/AdminLoginPage';

const PrivateRoute = ({ children }: { children: JSX.Element }) => {
    return authService.isAuthenticated() ? children : <Navigate to="/login" />;
};

const AdminPrivateRoute = ({ children }: { children: JSX.Element }) => {
    if (!authService.isAuthenticated()) {
        return <Navigate to="/admin/login" />;
    }
    if (!authService.isAdmin()) {
        // If logged in but not admin, maybe redirect to dashboard or show unauthorized
        // Ideally redirect to admin login to force admin auth?
        // Let's redirect to admin login.
        return <Navigate to="/admin/login" />;
    }
    return children;
};

function App() {
    return (
        <Router basename="/radarinvest">
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                
                {/* Admin Routes */}
                <Route path="/admin/login" element={<AdminLoginPage />} />
                <Route
                    path="/admin"
                    element={
                        <AdminPrivateRoute>
                            <AdminPage />
                        </AdminPrivateRoute>
                    }
                />

                <Route
                    path="/dashboard"
                    element={
                        <PrivateRoute>
                            <Dashboard />
                        </PrivateRoute>
                    }
                />
                
                <Route path="*" element={<Navigate to="/dashboard" />} />
            </Routes>
        </Router>
    );
}

export default App;
