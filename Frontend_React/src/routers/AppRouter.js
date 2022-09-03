import React from 'react';
import {
  BrowserRouter,
  Routes,
  Route,
  useLocation,
  Navigate
} from "react-router-dom";

import TabPanel from './../components/shared/TabPanel';
import Dashboard from './../components/dashboard/Dashboard';
import Book from './../components/Book';
import Login from './../components/common/Login';
import Register from './../components/common/Register';
import SignupSuccess from './../components/common/SignupSuccess';

import { ChatHome } from '../components/chat/ChatHome';

import EventHome from '../components/event/EventHome';
import AccountHome from '../components/account/AccountHome';

import { Claims } from '../utils/ClientCache'

const createHistory = require("history").createBrowserHistory;
export const history = createHistory();

const AppRouter = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<RequireAuth><TabPanel /></RequireAuth>}>
          {/* <Route path="/" element={ <TabPanel />}> */}
          <Route path="/" element={<Dashboard />} />
          <Route path="account" element={<AccountHome />} />
          <Route path="book" element={<Book />} />
          <Route path="chat" element={<ChatHome />}>
            <Route path=":id" element={<ChatHome />} />
          </Route>
          <Route path="event" element={<EventHome />} />
          {/* <Route path="dashboard" element={<Dashboard />} /> */}
        </Route>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/signup_success" element={<SignupSuccess />} />
      </Routes>
    </BrowserRouter>)
};

function RequireAuth({ children }) {
  let location = useLocation();
  if (Claims.getToken() ? false : true) {
    // Redirect them to the /login page, but save the current location they were
    // trying to go to when they were redirected. This allows us to send them
    // along to that page after they login, which is a nicer user experience
    // than dropping them off on the home page.
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
}

export default AppRouter;