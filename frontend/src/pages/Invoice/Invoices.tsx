import { useEffect, useState } from "react";
import httpClient from "../../services/http";
import { Invoice } from "../../helpers/types";
import User from "../../services/user";
import { InvoiceStatus } from "../../helpers/enums";
import PayInitModal from "../../components/Invoice/PayInitModal";
import UpdateInvoiceStatus from "../../components/Invoice/UpdateInvoiceStatus";


/**
 * An array of hosting plans.
 * Each plan object contains details such as ID, name, memory, CPU, storage, price, and whether it is the current plan.
 * Plans can include various configurations and pricing options for different needs.
 */
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


/**

Combines multiple CSS class names into a single string.
Filters out falsy values (null, undefined, false, 0, '') from the input array.
Joins the remaining class names with a space.
Returns a string of concatenated class names.
@param classes Array of CSS class names to be combined.
@returns A single string containing the combined class names.
*/
  function classNames(...classes) {
    return classes.filter(Boolean).join(' ')
  }
  
export default function Invoices() {
    const [invoicesRaw, setInvoicesRaw] = useState([]); // [1]
    const [courses, setCourses] = useState([]); // [2]
    const [registrations, setRegistrations] = useState([]); // [3]
    const [invoices, setInvoices] = useState<Invoice[]>([]); // [4

    // const []

    /**
 * Fetches a list of invoices from the server.
 * Uses the httpClient to make a GET request to the "/invoices" endpoint.
 * If the request is successful, sets the fetched invoices using the setInvoicesRaw function.
 * If there is an error, logs the error to the console.
 */
    useEffect(() => {
        httpClient("/invoices", "GET")
          .then((res) => {
            setInvoicesRaw(res.data);
          })
          .catch((err) => {
            console.log(err);
          });
      }, []);

    /**
 * Fetches a list of registrations from the server.
 * Uses the httpClient to make a GET request to the "/registrations" endpoint.
 * If the request is successful, sets the fetched registrations using the setRegistrations function.
 * If there is an error, logs the error to the console.
 */
      useEffect(() => {
        httpClient("/registrations", "GET")
            .then((res) => {
                setRegistrations(res.data);
            })
            .catch((err) => {
                console.log(err);
            });
    }
    , [invoicesRaw]);

    /**
 * Fetches a list of courses from the server when the registrations state changes.
 * Uses the httpClient to make a GET request to the "/courses" endpoint.
 * If the request is successful, sets the fetched courses using the setCourses function.
 * If there is an error, logs the error to the console.
 */
    useEffect(() => {
        httpClient("/courses", "GET")
            .then((res) => {
                setCourses(res.data);
            })
            .catch((err) => {
                console.log(err);
            });
    }
    , [registrations]);

    /**
 * Updates the invoices state based on the registrations and courses state.
 * Maps over the invoicesRaw array and for each invoice, finds the corresponding registration and course.
 * Updates the invoice object with the course name and ID, and the registration ID and rating.
 * Sets the updated invoices array as the new state.
 * Logs the updated invoices array to the console.
 */

    useEffect(() => {
        setInvoices(invoicesRaw.map((invoice) => {
            const registration = registrations.find((registration) => registration.id === invoice.registrationId);
            const course = courses.find((course) => course.id === registration?.course.id);
            return {
                ...invoice,
                course: {
                    name: course?.name,
                    id: course?.id
                },
                registration: {
                    id: registration?.id,
                    rating: registration?.rating
                }
            }
        }));

        console.log(invoices);
    },[registrations]);


    return (
      <div>
        <div className="px-4 sm:px-6 lg:px-8 pt-5">
        <div className="sm:flex sm:items-center">
          <div className="sm:flex-auto">
            <h1 className="text-base font-semibold leading-6 text-gray-900">Invoices</h1>
            <p className="mt-2 text-sm text-gray-700">
            {User().personType === 'Customer' ? 'A list of all your invoices.' : 'A list of all the sport center\'s invoices.'}
            </p>
          </div>
        </div>
        <div className="-mx-4 mt-10 ring-1 ring-gray-300 sm:mx-0 sm:rounded-lg">
          <table className="min-w-full divide-y divide-gray-300">
            <thead>
              <tr>
                <th scope="col" className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900 sm:pl-6">
                  Course
                </th>
                <th
                  scope="col"
                  className="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell"
                >
                  Amount
                </th>
                <th
                  scope="col"
                  className="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 lg:table-cell"
                >
                  Status
                </th>
                <th scope="col" className="relative py-3.5 pl-3 pr-4 sm:pr-6">
                  <span className="sr-only">Select</span>
                </th>
              </tr>
            </thead>
            <tbody>
              {invoices.map((invoice, planIdx) => (
                <tr key={invoice.id}>
                  <td
                    className={classNames(
                      planIdx === 0 ? '' : 'border-t border-transparent',
                      'relative py-4 pl-4 pr-3 text-sm sm:pl-6'
                    )}
                  >
                    <div className="font-medium text-gray-900">
                      {invoice.course.name}
                    </div>
                    
                  </td>
                  <td
                    className={classNames(
                      planIdx === 0 ? '' : 'border-t border-gray-200',
                      'hidden px-3 py-3.5 text-sm text-gray-500 lg:table-cell'
                    )}
                  >
                    ${invoice.amount.toFixed(2)}
                  </td>
                  <td
                    className={classNames(
                      planIdx === 0 ? '' : 'border-t border-gray-200',
                      'hidden px-3 py-3.5 text-sm text-gray-500 lg:table-cell',
                      invoice.status === InvoiceStatus.Completed ? 'text-green-600' :
                      invoice.status === InvoiceStatus.Open ? 'text-yellow-600'
                        : 'text-red-600'
                    )}
                  >
                    <b>{invoice.status}</b>
                  </td>
                  <td
                    className={classNames(
                      planIdx === 0 ? '' : 'border-t border-transparent',
                      'relative py-3.5 pl-3 pr-4 text-right text-sm font-medium sm:pr-6'
                    )}
                  >
                   {User().personType === "Owner" ? <UpdateInvoiceStatus 
                   invoiceId={invoice.id} 
                     parentStatus={invoice.status}
                     parentAmount={invoice.amount}
                     refresh={() => {
                          httpClient("/invoices", "GET")
                          .then((res) => {
                             setInvoicesRaw(res.data);
                          })
                          .catch((err) => {
                             console.log(err);
                          });
                     }}
                   />: null}
                   {User().personType === "Customer" && invoice.status === "Open" ? <PayInitModal invoiceId={invoice.id} />: null}
                    {planIdx !== 0 ? <div className="absolute -top-px left-0 right-6 h-px bg-gray-200" /> : null}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
      </div>
    )
  }
  
