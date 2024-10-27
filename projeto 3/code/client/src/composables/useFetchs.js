import axios from 'axios';

export const useFetchs = () => {
    const getInstitutions = async () => {
        const response = await axios.get('http://localhost:3000/enterprises/institutions');
        return response.data;
    };

    const createEnterprise = async (data) => {
        const response = await axios.post('http://localhost:3000/enterprises', data);
        return response.data;
    };

    const createStudent = async (data) => {
        const response = await axios.post('http://localhost:3000/students', data);
        return response.data;
    };

    const loginStudent = async (data) => {
        const response = await axios.post('http://localhost:3000/users/login/student', data);
        return response.data;
    };

    const loginEnterprise = async (data) => {
        const response = await axios.post('http://localhost:3000/users/login/enterprise', data);
        return response.data;
    };

    const deleteUser = async (id, type) => {
        const response = await axios.delete(`http://localhost:3000/${type}/${id}`);
        return response.data;
    };

    return {
        getInstitutions,
        createEnterprise,
        createStudent,
        loginStudent,
        loginEnterprise,
        deleteUser,
    };
};
