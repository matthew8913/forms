<template>
    <div class="container mt-5">
        <h1 class="text-center mb-4">Список опросов</h1>
        <div v-if="forms.length === 0" class="text-center">
            <p>Нет доступных опросов</p>
        </div>
        <div v-else>
            <div v-for="form in forms" :key="form.id" class="mb-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">{{ form.title }}</h5>
                        <p class="card-text">{{ form.description }}</p>
                        <div v-if="userRole === 'USER'">
                            <router-link :to="`/complete-form/${form.id}`" class="btn btn-primary">Заполнить
                                опрос</router-link>
                        </div>
                        <div v-else-if="userRole === 'CREATOR'">
                            <router-link :to="`/statistics/${form.id}`" class="btn btn-primary">Посмотреть
                                статистику</router-link>
                        </div>
                    </div>
                </div>
            </div>
            <div v-if="userRole === 'CREATOR'" class="mt-4 text-center">
                <router-link to="/create-form" class="btn btn-success">Создать опрос</router-link>
            </div>
        </div>
    </div>
</template>

<script>
import { API_BASE_URL } from '../../config.js';
import { authService } from '../service/authService.js'

export default {
    data() {
        return {
            forms: [],
            userRole: null, 
        };
    },
    async created() {
        this.userRole = authService.getUserRole();
        await this.fetchForms();
    },
    methods: {
        async fetchForms() {
            try {
                const response = await fetch(`${API_BASE_URL}/forms`);
                if (!response.ok) {
                    const errorData = await response.json();
                    console.error('Ошибка при загрузке списка опросов:', errorData);
                    return;
                }
                const data = await response.json();
                this.forms = data;
            } catch (error) {
                console.error('Ошибка при загрузке списка опросов:', error);
            }
        },
    },
};
</script>