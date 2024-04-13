import axios from "axios";


export default async function httpClient(
    url: string,
    method: "GET" | "POST" | "PUT" | "DELETE" | "PATCH" = "GET",
    data?: Record<string, any> | string
) {
    return axios({
        url: `http://localhost:8080${url}`,
        method,
        data,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
    });
}