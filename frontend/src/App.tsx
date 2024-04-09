import {createBrowserRouter, RouterProvider} from "react-router-dom";
import AuthPage from "./pages/AuthPage";
import HomePage from "./pages/HomePage";
import Skeleton from "./components/Skeleton";
import Courses from "./pages/Course/Courses";
import Signup from "./pages/Signup";
import Profile from "./pages/Profile/Profile";
import CreateCourseModal from "./pages/Course/CreateCourse";

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
        path: "/courses/create",
        element: <Skeleton><CreateCourseModal/></Skeleton>,
    },
    // {
    //     path: "/courses/:id",
    //     element: <Skeleton><ViewCourse/></Skeleton>,
    // },
    {
        path: "/login",
        element: <AuthPage/>,
    },
    {
        path: "/signup",
        element: <Signup/>,
    },
    {
        path: "/profile",
        element: <Skeleton><Profile/></Skeleton>,
    }
]);

export default function App() {
    return <RouterProvider router={router} />;
}