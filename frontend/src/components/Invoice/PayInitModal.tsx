import { Fragment, useState } from 'react'
import { Dialog, Transition } from '@headlessui/react'
import { CheckIcon, QuestionMarkCircleIcon } from '@heroicons/react/24/outline'
import React from 'react'
import httpClient from '../../services/http'

export default function PayInitModal({
    invoiceId,
}: {
    invoiceId: string,
}) {

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

  return (
    <React.Fragment>

        <button
            onClick={() => setOpen(true)}
            type="button"
            className="inline-flex items-center rounded-md bg-white px-2.5 py-1.5 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-30 disabled:hover:bg-white"
        //   disabled={plan.isCurrent}
        >
            Pay
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
                <div>
                  <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-purple-100">
                    <QuestionMarkCircleIcon className="h-6 w-6 text-purple-600" aria-hidden="true" />
                  </div>
                  <div className="mt-3 text-center sm:mt-5">
                    <Dialog.Title as="h3" className="text-base font-semibold leading-6 text-gray-900">
                      Proceed to payment
                    </Dialog.Title>
                    <div className="mt-2">
                      <p className="text-sm text-gray-500">
                        You will be redirected to the payment gateway to complete the payment.
                      </p>
                    </div>
                  </div>
                </div>
                <div className="mt-5 sm:mt-6 flex flex-row justify-between gap-3">
                <button
                    type="button"
                    className="inline-flex justify-center w-full rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50"
                    onClick={() => setOpen(false)}
                >
                    Cancel
                </button>
                <button
                    type="button"
                    className="inline-flex justify-center w-full rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                    onClick={initPayment}
                >
                    Continue
                </button>
                </div>

              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition.Root>
    </React.Fragment>
   
  )
}
