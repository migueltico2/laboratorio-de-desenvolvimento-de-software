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

    const login = async (data) => {
        const response = await axios.post('http://localhost:3000/login', data);
        return response.data;
    };

    return {
        getInstitutions,
        createEnterprise,
        createStudent,
    };
};
