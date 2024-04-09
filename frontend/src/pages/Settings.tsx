import { useEffect, useState } from "react";
import CreateRoomModal from "../components/CreateRoomModal"
import { RoomDTO } from "../helpers/types";
import httpClient from "../services/http";

const plans = [
    {
      id: 1,
      name: 'Hobby',
      memory: '4 GB RAM',
      cpu: '4 CPUs',
      storage: '128 GB SSD disk',
      price: '$40',
      isCurrent: false,
    },
    {
      id: 2,
      name: 'Startup',
      memory: '8 GB RAM',
      cpu: '6 CPUs',
      storage: '256 GB SSD disk',
      price: '$80',
      isCurrent: true,
    },
    // More plans...
  ]
  
  function classNames(...classes) {
    return classes.filter(Boolean).join(' ')
  }
  
  export default function Settings() {

    const [rooms, setRooms] = useState<RoomDTO[]>([]); // [1]


    useEffect(() => {
        
        httpClient("/rooms", "GET")
          .then((res) => {
            setRooms(res.data);
          })
          .catch((err) => {
            console.log(err);
          });

    }, []);

    return (
      <div className="px-4 sm:px-6 lg:px-8 py-5">
        <div className="sm:flex sm:items-center">
          <div className="sm:flex-auto">
            <h1 className="font-semibold leading-1 text-gray-900 text-xl py-5">Sport center settings</h1>
            <hr/>
            <h2 className="text-base font-semibold leading-6 text-gray-900 pt-5">Rooms</h2>
            
          </div>
          <div className="mt-4 sm:ml-16 sm:mt-0 sm:flex-none">
          <CreateRoomModal/>
          </div>
        </div>
        <div className="-mx-4 mt-10 ring-1 ring-gray-300 sm:mx-0 sm:rounded-lg">
          <table className="min-w-full divide-y divide-gray-300">
            <thead>
              <tr>
                <th scope="col" className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">
                  Room ID
                </th>
                <th
                  scope="col"
                  className="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell"
                >
                  Room name
                </th>
               
              </tr>
            </thead>
            <tbody>
              {rooms.map((room, planIdx) => (
                <tr key={room.id}>
                  <td
                    className={classNames(
                      'relative py-4 pl-4 pr-3 text-sm sm:pl-6'
                    )}
                  >
                    <div className="font-medium text-gray-900">
                      {room.id}
                    </div>
                    
                  </td>
                  <td
                    className={classNames(
                      
                      'hidden px-3 py-3.5 text-sm text-gray-500 lg:table-cell'
                    )}
                  >
                    <div className="text-gray-900">{room.name}</div>
                  </td>
                  
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    )
  }
  