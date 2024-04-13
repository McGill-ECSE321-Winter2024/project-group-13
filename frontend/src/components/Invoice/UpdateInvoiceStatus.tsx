import { Fragment, useEffect, useState } from 'react'
import { Dialog, Transition } from '@headlessui/react'
import { CheckIcon, QuestionMarkCircleIcon } from '@heroicons/react/24/outline'
import React from 'react'
import httpClient from '../../services/http'
import { InvoiceStatus } from '../../helpers/enums'

export default function UpdateInvoiceStatus({
    invoiceId,
    parentAmount,
    parentStatus,
    refresh
}: {
    invoiceId: string,
    parentAmount: number,
    parentStatus: InvoiceStatus,
    refresh: () => void
}) {

    const [amount, setAmount] = useState<number>(parentAmount)
    const [status, setStatus] = useState(parentStatus)

    const [open, setOpen] = useState(false)

    const initPayment = () => {
        // Initiate payment
        httpClient(`/invoices/${invoiceId}/pay`, 'POST')
        .then((res) => {
            console.log(res.data);
            window.location.href = res.data;
        })
        .catch((err) => {
            console.log(err);
        });
    }

    useEffect(() => {
        setAmount(parentAmount)
        setStatus(parentStatus)
    }, [open]);

    const updateInvoice = async (e) => {
      e.preventDefault();
      try {
        await httpClient(`/invoices/${invoiceId}/amount?amount=${amount}`, 'PUT')
        await httpClient(`/invoices/${invoiceId}/status?status=${status}`, 'PUT')
        refresh();
        setOpen(false);

      }
      catch (e) {
        alert(e.response.data.message);
        return;
      }
    }

  return (
    <React.Fragment>

        <button
            onClick={() => setOpen(true)}
            type="button"
            className="inline-flex items-center rounded-md bg-white px-2.5 py-1.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-30 disabled:hover:bg-white"
        //   disabled={plan.isCurrent}
        >
            Update invoice
        </button>
        <Transition.Root show={open} as={Fragment}>
      <Dialog as="div" className="relative z-10" onClose={setOpen}>
        <Transition.Child
          as={Fragment}
          enter="ease-out duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-200"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
        </Transition.Child>

        <div className="fixed inset-0 z-10 w-screen overflow-y-auto">
          <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
              enterTo="opacity-100 translate-y-0 sm:scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 translate-y-0 sm:scale-100"
              leaveTo="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
            >
              <Dialog.Panel className="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-sm sm:p-6">
              <h3 className='text-lg font-semibold leading-6 text-gray-900 pb-3'>
                Update invoice
              </h3>
              <form className="  sm:rounded-xl md:col-span-2" onSubmit={updateInvoice}>
          <div className="">
            <div className="grid max-w-2xl grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">

              <div className="col-span-full">
                <label htmlFor="street-address" className="block text-sm font-medium leading-6 text-gray-900">
                  Amount
                </label>
                <div className="mt-2">
                  <input
                  required
                    type="number"
                    name="amount"
                    max="100000"
                    id="amount"
                    value={amount}
                    onChange={(e) => setAmount(parseFloat(e.target.value))}

                    className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                  />
                </div>
              </div>

             
            </div>
            
          </div>
          <div className="">
            <div className="grid max-w-2xl grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6">

              <div className="col-span-full">
                <label htmlFor="street-address" className="block text-sm font-medium leading-6 text-gray-900">
                  Status
                </label>
                <div className="mt-2">
                  <select
                    name="status"
                    id="status"
                    value={status}
                    onChange={(e) => setStatus(e.target.value as InvoiceStatus)}
                    className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                  >
                    {Object.values(InvoiceStatus).map((status) => (
                      <option key={status} value={status}>
                        {status}
                      </option>
                    ))}
                  </select>


                </div>
              </div>

             
            </div>
            
          </div>
          <div className="flex items-center justify-end gap-x-6 border-t border-gray-900/10 px-4 py-4 sm:px-8">
            <button type="button" className="text-sm font-semibold leading-6 text-gray-900"
            onClick={() => setOpen(false)}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
            >
              Save
            </button>
          </div>
        </form>
              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition.Root>
    </React.Fragment>
   
  )
}
