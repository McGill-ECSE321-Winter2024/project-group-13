import {createBrowserRouter, RouterProvider} from "react-router-dom";
import AuthPage from "./pages/AuthPage";
import HomePage from "./pages/HomePage";
import Skeleton from "./components/Skeleton";
import {CourseHomePage} from "./pages/CourseHomePage";
import Courses from "./pages/Courses";
import Signup from "./pages/Signup";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Skeleton><HomePage/></Skeleton>,
    },
    {
        path: "/courses",
        element: <Skeleton><Courses/></Skeleton>,
    },
    {
        path: "/login",
        element: <AuthPage/>,
    },
    {
        path: "/signup",
        element: <Signup/>,
    },
    {
        path: "/courses",
        element: <Skeleton><CourseHomePage/></Skeleton>,
    }
]);

export default function App() {
    return <RouterProvider router={router} />;
}