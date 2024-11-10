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

    const deleteUser = async (id, user_id, type) => {
        const response = await axios.delete(`http://localhost:3000/${type}/${id}`);
        const response2 = await axios.delete(`http://localhost:3000/users/${user_id}`);
        return response.data;
    };

    const updateUser = async (data) => {
        const payload = {};

        const userData = {
            name: data.name,
        };

        if (data.type === 'student') {
            payload.CPF = data.CPF;
            payload.RG = data.RG;
            payload.address = data.address;
            payload.course = data.course;
        } else {
            payload.CNPJ = data.CNPJ;
        }


        await axios.put(`http://localhost:3000/users/${data.user_id}`, userData);
        const response2 = await axios.put(`http://localhost:3000/${data.type}/${data.id}`, payload);
        return response2.data;
    };

    const listAdvantages = async (enterprise_id) => {
        let response;
        if (enterprise_id) {
            response = await axios.get(`http://localhost:3000/advantage/list/${enterprise_id}`);
        } else {
            response = await axios.get('http://localhost:3000/advantage');
        }
        return response.data;
    };

    const createAdvantage = async (formData) => {
        try {
            const response = await axios.post('http://localhost:3000/advantage', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
                transformRequest: [function (data) {
                    return data;
                }],
            });
            return response.data;
        } catch (error) {
            console.error('Erro na requisição:', error);
            throw error;
        }
    };

    return {
        getInstitutions,
        createEnterprise,
        createStudent,
        loginStudent,
        loginEnterprise,
        deleteUser,
        updateUser,
        listAdvantages,
        createAdvantage,
    };
};