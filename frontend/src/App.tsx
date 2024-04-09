import {createBrowserRouter, RouterProvider} from "react-router-dom";
import AuthPage from "./pages/AuthPage";
import HomePage from "./pages/HomePage";
import Skeleton from "./components/Skeleton";
import {CourseHomePage} from "./pages/CourseHomePage";


const router = createBrowserRouter([
    {
        path: "/",
        element: <Skeleton><HomePage/></Skeleton>,
    },
    {
        path: "/login",
        element: <AuthPage/>,
    },
    {
        path: "/courses",
        element: <Skeleton><CourseHomePage/></Skeleton>,
    }
]);

export default function App() {
    return <RouterProvider router={router} />;
}