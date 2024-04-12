import { Fragment, useEffect, useState } from 'react'
import { Dialog, Menu, Transition } from '@headlessui/react'
import {
  ArrowDownCircleIcon,
  ArrowPathIcon,
  ArrowUpCircleIcon,
  Bars3Icon,
  EllipsisHorizontalIcon,
  PlusSmallIcon,
} from '@heroicons/react/20/solid'
import { ArrowTopRightOnSquareIcon, BanknotesIcon, BellIcon, LinkIcon, XMarkIcon } from '@heroicons/react/24/outline'
import User from '../services/user'
import httpClient from '../services/http'
import { CourseDTO, Invoice } from '../helpers/types'

const stats = [
  { name: 'Revenue', value: '$405,091.00', change: '+4.75%', changeType: 'positive' },
  { name: 'Overdue invoices', value: '$12,787.00', change: '+54.02%', changeType: 'negative' },
  { name: 'Outstanding invoices', value: '$245,988.00', change: '-1.39%', changeType: 'positive' },
  { name: 'Expenses', value: '$30,156.00', change: '+10.18%', changeType: 'negative' },
]
const statuses = {
  Completed: 'text-green-700 bg-green-50 ring-green-600/20',
  Open: 'text-gray-600 bg-gray-50 ring-gray-500/10',
  Void: 'text-red-700 bg-red-50 ring-red-600/10',
  Pending: 'text-red-700 bg-red-50 ring-red-600/10',

}

const clients = [
  {
    id: 1,
    name: 'Tuple',
    imageUrl: 'https://tailwindui.com/img/logos/48x48/tuple.svg',
    lastInvoice: { date: 'December 13, 2022', dateTime: '2022-12-13', amount: '$2,000.00', status: 'Overdue' },
  },
  {
    id: 2,
    name: 'SavvyCal',
    imageUrl: 'https://tailwindui.com/img/logos/48x48/savvycal.svg',
    lastInvoice: { date: 'January 22, 2023', dateTime: '2023-01-22', amount: '$14,000.00', status: 'Paid' },
  },
  {
    id: 3,
    name: 'Reform',
    imageUrl: 'https://tailwindui.com/img/logos/48x48/reform.svg',
    lastInvoice: { date: 'January 23, 2023', dateTime: '2023-01-23', amount: '$7,600.00', status: 'Paid' },
  },
]

function classNames(...classes) {
  return classes.filter(Boolean).join(' ')
}



export default function Example() {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const [invoices, setInvoices] = useState<Invoice[]>([]);
  const [customers, setCustomers] = useState([]);
  const [stats, setStats] = useState([]);
  const [courses, setCourses] = useState<CourseDTO[]>([]);

  useEffect(() => {
    if(User().personType === "Instructor") window.location.href = '/courses';
    if(User().personType === "Customer") httpClient('/courses')
        .then(async (res) => {
            const registrations = await httpClient('/registrations');
            const courses = res.data.filter(course => course.courseState === 'Approved' && !registrations.data.some(reg => reg.course.id === course.id));

            // randomly select 5 courses from the lsiot
            const selectedCourses = courses.sort(() => Math.random() - Math.random()).slice(0, 5);
            setCourses(selectedCourses);


        })
        .catch((err) => {
            console.log(err);
        })
    httpClient('/invoices').then(async (response) => {
        // await new Promise(r => setTimeout(r, 1000));
        console.log(response.data); 
        setInvoices(response.data);
        let revenue = 0
        let pendingPayments = 0
        const invoices = response.data;
        invoices.forEach(invoice => {
            if (invoice.status === "Open") {
                pendingPayments += invoice.amount;
                console.log(pendingPayments);
            }
            revenue += invoice.amount;
        });
        console.log(revenue, pendingPayments);
        try {
            const cus = await httpClient('/customers');
        
        const numberOfCustomers = cus.data.length;
        const coursesCount = await httpClient('/courses')

        const stats = [
            { name: 'Revenue', value: `$${revenue}`, change: '+4.75%', changeType: 'positive' },
            { name: 'Pending payments', value: `$${pendingPayments}`, change: '+54.02%', changeType: 'negative' },
            { name: 'Customers', value: numberOfCustomers, change: '+10.18%', changeType: 'negative' },
            { name: 'Courses', value: coursesCount.data.length, change: '+10.18%', changeType: 'negative' },
        ];
        setStats(stats);
        setStats(stats);
        } catch (error) {
            console.error('Error fetching stats:', error);
        }
            
    }
    )
    }
    , []);

  return (
    <>

      <main>
        <div className="relative isolate overflow-hidden pt-5">
          {/* Secondary navigation */}
          <header className="pb-4 pt-6 sm:pb-6">
            <div className="mx-auto flex max-w-7xl flex-wrap items-center gap-6 px-4 sm:flex-nowrap sm:px-6 lg:px-8">
              <h1 className="text-base font-semibold leading-7 text-gray-900">Welcome {User().personName}</h1>
            </div>
          </header>

          {/* Stats */}
          <div className="border-b border-b-gray-900/10 lg:border-t lg:border-t-gray-900/5">
            <dl className="mx-auto grid max-w-7xl grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 lg:px-2 xl:px-0">
              {stats.map((stat, statIdx) => (
                <div
                  key={stat.name}
                  className={classNames(
                    statIdx % 2 === 1 ? 'sm:border-l' : statIdx === 2 ? 'lg:border-l' : '',
                    'flex items-baseline flex-wrap justify-between gap-y-2 gap-x-4 border-t border-gray-900/5 px-4 py-10 sm:px-6 lg:border-t-0 xl:px-8'
                  )}
                >
                  <dt className="text-sm font-medium leading-6 text-gray-500">{stat.name}</dt>
                  
                  <dd className="w-full flex-none text-3xl font-medium leading-10 tracking-tight text-gray-900">
                    {stat.value}
                  </dd>
                </div>
              ))}
            </dl>
          </div>

          <div
            className="absolute left-0 top-full -z-10 mt-96 origin-top-left translate-y-40 -rotate-90 transform-gpu opacity-20 blur-3xl sm:left-1/2 sm:-ml-96 sm:-mt-10 sm:translate-y-0 sm:rotate-0 sm:transform-gpu sm:opacity-50"
            aria-hidden="true"
          >
            <div
              className="aspect-[1154/678] w-[72.125rem] bg-gradient-to-br from-[#FF80B5] to-[#9089FC]"
              style={{
                clipPath:
                  'polygon(100% 38.5%, 82.6% 100%, 60.2% 37.7%, 52.4% 32.1%, 47.5% 41.8%, 45.2% 65.6%, 27.5% 23.4%, 0.1% 35.3%, 17.9% 0%, 27.7% 23.4%, 76.2% 2.5%, 74.2% 56%, 100% 38.5%)',
              }}
            />
          </div>
        </div>

        {courses.length ? <div>
       <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 pt-10">
        <div className="sm:flex sm:items-center">
          <div className="sm:flex-auto">
            <h1 className="text-base font-semibold leading-6 text-gray-900">New courses</h1>
            <p className="mt-2 text-sm text-gray-700">
              Here is a list of courses you might be interested in
            </p>
          </div>
          <div className="mt-4 sm:ml-16 sm:mt-0 sm:flex-none">
            <a
                href="/courses"
              type="button"
              className="block rounded-md bg-indigo-600 px-3 py-2 text-center text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              View all courses
            </a>
          </div>
        </div>
      </div>
      <div className="mt-8 flow-root overflow-hidden">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <table className="w-full text-left">
            <thead className="bg-white">
              <tr>
                <th scope="col" className="relative isolate py-3.5 pr-3 text-left text-sm font-semibold text-gray-900">
                  Name
                  <div className="absolute inset-y-0 right-full -z-10 w-screen border-b border-b-gray-200" />
                  <div className="absolute inset-y-0 left-0 -z-10 w-screen border-b border-b-gray-200" />
                </th>
                <th
                  scope="col"
                  className="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 sm:table-cell"
                >
                  Description
                </th>
                <th
                  scope="col"
                  className="hidden px-3 py-3.5 text-left text-sm font-semibold text-gray-900 md:table-cell"
                >
                  Level
                </th>
                <th scope="col" className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">
                  Pricing
                </th>
              </tr>
            </thead>
            <tbody>
              {courses.map((person) => (
                <tr key={person.id}>
                  <td className="relative py-4 pr-3 text-sm font-medium text-gray-900">
                    {person.name}
                    <div className="absolute bottom-0 right-full h-px w-screen bg-gray-100" />
                    <div className="absolute bottom-0 left-0 h-px w-screen bg-gray-100" />
                  </td>
                  <td className="hidden px-3 py-4 text-sm text-gray-500 sm:table-cell">{person.description}</td>
                  <td className="hidden px-3 py-4 text-sm text-gray-500 md:table-cell">{person.level}</td>
                  <td className="px-3 py-4 text-sm text-gray-500">${person.hourlyRateAmount.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div> : null}

        <div className="space-y-16 py-16 xl:space-y-20">
          {/* Recent activity table */}
          <div>
            <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
              <h2 className="mx-auto max-w-2xl text-base font-semibold leading-6 text-gray-900 lg:mx-0 lg:max-w-none">
                Recent invoices
              </h2>
              
            </div>
            <div className="mt-6 overflow-hidden border-t border-gray-100">
              <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
                <div className="mx-auto max-w-2xl lg:mx-0 lg:max-w-none">
                  <table className="w-full text-left">
                    <thead className="sr-only">
                      <tr>
                        <th>Amount</th>
                        <th className="hidden sm:table-cell">Client</th>
                        <th>More details</th>
                      </tr>
                    </thead>
                    <tbody>
                      {invoices.map((transaction) => (
                            <tr key={transaction.id}>
                              <td className="relative py-5 pr-6">
                                <div className="flex gap-x-6">
                                 <BanknotesIcon className="h-6 w-6 text-gray-900" aria-hidden="true" />
                                  <div className="flex-auto">
                                    <div className="flex items-start gap-x-3">
                                      <div className="text-sm font-medium leading-6 text-gray-900">
                                        ${transaction.amount}
                                      </div>
                                      <div
                                        className={classNames(
                                          statuses[transaction.status],
                                          'rounded-md py-1 px-2 text-xs font-medium ring-1 ring-inset'
                                        )}
                                      >
                                        {transaction.status}
                                      </div>
                                    </div>
                                    
                                  </div>
                                </div>
                                <div className="absolute bottom-0 right-full h-px w-screen bg-gray-100" />
                                <div className="absolute bottom-0 left-0 h-px w-screen bg-gray-100" />
                              </td>
                              {/* <td className="hidden py-5 pr-6 sm:table-cell">
                                <div className="text-sm leading-6 text-gray-900">{transaction.client}</div>
                                <div className="mt-1 text-xs leading-5 text-gray-500">{transaction.description}</div>
                              </td> */}
                              <td className="py-5 text-right">
                                <div className="flex justify-end">
                                  {/* <a
                                    href={transaction.href}
                                    className="text-sm font-medium leading-6 text-indigo-600 hover:text-indigo-500"
                                  >
                                    View<span className="hidden sm:inline"> transaction</span>
                                    <span className="sr-only">
                                      , invoice #{transaction.invoiceNumber}, {transaction.client}
                                    </span>
                                  </a> */}
                                </div>
                                <div className="mt-1 text-xs leading-5 text-gray-500">
                                  Invoice <span className="text-gray-900">{transaction.id}</span>
                                </div>
                              </td>
                            </tr>
                          ))}
                    </tbody>
                  </table>
                </div>
                <a 
                href='/invoices'
                className="mt-1 inline-flex items-center gap-x-2 text-sm font-medium leading-5 text-indigo-600 hover:text-indigo-500">
                <ArrowTopRightOnSquareIcon className="h-5 w-5" aria-hidden="true" />
                <span>View all invoices</span>
                </a>
              </div>
              
            </div>
            
          </div>

        </div>
      </main>
    </>
  )
}
