import {createBrowserRouter, RouterProvider} from "react-router-dom";
import AuthPage from "./pages/AuthPage";
import HomePage from "./pages/HomePage";
import Skeleton from "./components/Skeleton";
import Courses from "./pages/Course/Courses";
import Signup from "./pages/Signup";
import Profile from "./pages/Profile/Profile";
import CreateCourseModal from "./pages/Course/CreateCourse";
import Settings from "./pages/Settings";
import ViewCourse from "./pages/Course/ViewCourse";
import Invoices from "./pages/Invoice/Invoices";
import People from "./pages/People/People";
import UserCalendar from "./pages/Course/UserCalendar"; // Import the UserSchedule component

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
    {
        path: "/courses/:courseId",
        element: <Skeleton><ViewCourse/></Skeleton>,
    },
    {
        path: "/invoices",
        element: <Skeleton><Invoices/></Skeleton>,
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
        path: "/profile",
        element: <Skeleton><Profile/></Skeleton>,
    },
    {
        path: "/settings",
        element: <Skeleton><Settings/></Skeleton>,
    },
    {
        path: "/people",
        element: <Skeleton><People/></Skeleton>,
    },
    {
        path: "/mycalendar",
        element: <Skeleton><UserCalendar/></Skeleton>,
    },
    {
        path: "/*",
        element: <Skeleton>
            <div className="flex items-center justify-center h-full">
                <br/>
                <h1 className="text-4xl font-bold text-gray-800">404 Not Found</h1>
            </div>
        </Skeleton>,
    }
]);


export default function App() {
    return <RouterProvider router={router} />;
}