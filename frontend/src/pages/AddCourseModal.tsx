import React, { useState } from 'react';

const AddCourseModal = ({ onClose }) => {
    const [courseData, setCourseData] = useState({
        name: '',
        description: '',
        level: '',
        startDate: '',
        endDate: '',
        room: '',
        instructor: '',
        cost: '',
        state: ''
    });

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setCourseData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log(courseData); // Implement the logic to save this data
        onClose(); // Close the modal after submitting
    };

    return (
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center p-4">
            <div className="bg-white p-5 rounded-lg shadow-lg max-w-lg w-full">
                <form onSubmit={handleSubmit}>
                    <h2 className="text-xl font-bold mb-4">Add New Course</h2>
                    <input className="block w-full p-2 border" type="text" name="name" placeholder="Name" onChange={handleInputChange} value={courseData.name} />
                    <textarea className="block w-full p-2 border" name="description" placeholder="Description" onChange={handleInputChange} value={courseData.description}></textarea>
                    <select className="block w-full p-2 border" name="level" onChange={handleInputChange} value={courseData.level}>
                        <option value="">Select Level</option>
                        <option value="Beginner">Beginner</option>
                        <option value="Intermediate">Intermediate</option>
                        <option value="Advanced">Advanced</option>
                    </select>
                    <input className="block w-full p-2 border" type="date" name="startDate" onChange={handleInputChange} value={courseData.startDate} />
                    <input className="block w-full p-2 border" type="date" name="endDate" onChange={handleInputChange} value={courseData.endDate} />
                    <select className="block w-full p-2 border" name="room" onChange={handleInputChange} value={courseData.room}>
                        {/* Populate these options based on available rooms */}
                        <option value="">Select Room</option>
                        <option value="101">Room 101</option>
                        <option value="102">Room 102</option>
                    </select>
                    <select className="block w-full p-2 border" name="instructor" onChange={handleInputChange} value={courseData.instructor}>
                        {/* Populate these options based on available instructors */}
                        <option value="">Select Instructor</option>
                        <option value="Dr. Jane Smith">Dr. Jane Smith</option>
                        <option value="Dr. John Doe">Dr. John Doe</option>
                    </select>
                    <input className="block w-full p-2 border" type="number" name="cost" placeholder="Hourly Rate Amount" onChange={handleInputChange} value={courseData.cost} />
                    <select className="block w-full p-2 border" name="state" onChange={handleInputChange} value={courseData.state}>
                        <option value="">Select State</option>
                        <option value="approved">Approved</option>
                        <option value="denied">Denied</option>
                        <option value="inactive">Inactive</option>
                        <option value="awaiting approval">Awaiting Approval</option>
                    </select>
                    <button type="submit" className="mt-4 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-700">Submit</button>
                    <button onClick={onClose} className="mt-4 ml-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700">Cancel</button>
                </form>
            </div>
        </div>
    );
};

export default AddCourseModal;
