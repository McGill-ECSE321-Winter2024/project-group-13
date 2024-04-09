import React from 'react';
import { Schedule } from '../helpers/types';
import _ from 'lodash';

export default function ScheduleSelector({
    schedule,
    setSchedule,
}: {
    schedule: Schedule,
    setSchedule: (schedule: Schedule) => void
}) {

  const [unusedDays, setUnusedDays] = React.useState<string[]>([])
  
  React.useEffect(() => {
    const days = ['monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday']
    setUnusedDays(days.filter(day => !schedule[day]))
  }, [schedule])

  const processTimeValue = (time: string) => {
    // only allows form 00:00 to 23:59
    const [hour, minute] = time.split(':').map(Number)
    return hour * 60 + minute
  }

  console.log(schedule)
    return (
        <form>
          // add a new day, start and end time, remove used days
          <div className="space-y-13">
            <select className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6">
              {unusedDays.map(day => (
                <option key={day} value={day}>{_.capitalize(day)}</option>
              ))}
            </select>
          </div>
            <div className="space-y-13">
                <div className="grid">
                    {Object.entries(schedule).map(([day, times]) => (
                        <div key={day} className="flex flex-col ">
                            <h2 className="text-lg font-medium">{_.capitalize(day)}</h2>
                            <div className="flex space-x-4">
                                <div>
                                    <label  className="block text-sm font-medium leading-6 text-gray-900">Start time</label>
                                    

                                    <form className="max-w-[8rem] mx-auto">
    <label className="block mb-2 text-sm font-medium text-gray-900">Select time:</label>
    <div className="relative">
        <div className="absolute inset-y-0 end-0 top-0 flex items-center pe-3.5 pointer-events-none">
            <svg className="w-4 h-4 text-gray-500" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                <path fillRule="evenodd" d="M2 12C2 6.477 6.477 2 12 2s10 4.477 10 10-4.477 10-10 10S2 17.523 2 12Zm11-4a1 1 0 1 0-2 0v4a1 1 0 0 0 .293.707l3 3a1 1 0 0 0 1.414-1.414L13 11.586V8Z" clipRule="evenodd"/>
            </svg>
        </div>
        <input type="time" id="time" className="bg-white border leading-none border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 placeholder-gray-400 focus:ring-2 focus:ring-blue-500 focus:border-blue-500" min="09:00" value={
            `${String(Math.floor(times.start / 60)).padStart(2, '0')}:${String(times.start % 60).padStart(2, '0')}`
        } max="18:00" step="900" onInput={(e: any) => setSchedule({...schedule, [day]: {start: processTimeValue(e.target.value), end: times.end}})} />
    </div>
</form>



                                </div>
                                <div>
                                    <label className="block text-sm font-medium leading-6 text-gray-900">Last name</label>
                                    <input
                                        type="text"
                                        autoComplete="family-name"
                                        className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                                    />
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
            <div className="mt-6 flex items-center justify-end gap-x-6">
                <button type="button" className="text-sm font-semibold leading-6 text-gray-900">Cancel</button>
                <button
                    type="submit"
                    className="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
                >
                    Save
                </button>
            </div>
        </form>
    );
}
