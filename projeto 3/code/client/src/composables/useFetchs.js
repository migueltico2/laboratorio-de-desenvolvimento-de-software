import axios from 'axios';

export const useFetchs = () => {
    const getInstitutions = async () => {
        const response = await axios.get('http://localhost:3000/enterprise/institutions');
        return response.data;
    };

    const createEnterprise = async (data) => {
        const response = await axios.post('http://localhost:3000/enterprise', data);
        return response.data;
    };

    const createStudent = async (data) => {
        const response = await axios.post('http://localhost:3000/student', data);
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

    const updateUser = async (data) => {
        console.log(data);
        const response = await axios.put(`http://localhost:3000/${data.type}/${data.id}`, data);
        return response.data;
    };

    return {
        getInstitutions,
        createEnterprise,
        createStudent,
        loginStudent,
        loginEnterprise,
        deleteUser,
        updateUser,
    };
};
