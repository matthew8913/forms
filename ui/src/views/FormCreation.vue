<template>
    <div class="container mt-5">
        <h1 class="text-center mb-4">Конструктор опросов</h1>
        <div class="mb-3">
            <label for="formTitle" class="form-label">Название опроса:</label>
            <input v-model="title" id="formTitle" class="form-control" placeholder="Введите название опроса" />
        </div>
        <div class="mb-3">
            <label for="formDescription" class="form-label">Описание опроса:</label>
            <textarea v-model="description" id="formDescription" class="form-control" placeholder="Введите описание опроса"></textarea>
        </div>
        <div v-for="(question, index) in questions" :key="index" class="mb-3">
            <QuestionComponent :question="question" @updateQuestion="updateQuestion(index, $event)"
                @removeQuestion="removeQuestion(index)" />
        </div>
        <div class="d-flex justify-content-center">
            <button @click="addQuestion" class="btn btn-light btn-lg mb-3 w-100" style="opacity: 0.7;">
                <span class="text-secondary">+</span>
            </button>
        </div>
        <div class="d-flex justify-content-center">
            <button @click="createform" class="btn btn-success">Создать опрос</button>
        </div>
    </div>
</template>

<script>
import QuestionComponent from '../components/QuestionCreation.vue';
import { API_BASE_URL } from '../../config.js';
import { authService } from '@/service/authService';

export default {
    components: {
        QuestionComponent,
    },
    data() {
        return {
            questions: [], 
            creatorId: authService.getUserId(),
            creatorName: authService.getUsername(), 
            title: '', 
            description: '', 
        };
    },
    methods: {
        // Метод для добавления нового вопроса в опрос
        addQuestion() {
            this.questions.push({
                id: null,
                formId: null,
                text: '',
                type: 'SINGLE_CHOICE',
                imageUrl: null,
                options: [],
            });
        },

        // Метод для обновления вопроса по индексу
        updateQuestion(index, updatedQuestion) {
            this.questions[index] = updatedQuestion;
        },

        // Метод для удаления вопроса по индексу
        removeQuestion(index) {
            this.questions.splice(index, 1);
        },

        // Метод для создания опроса
        async createform() {
            const form = {
                id: null,
                creatorId: this.creatorId,
                creatorName: this.creatorName,
                title: this.title,
                description: this.description,
                questions: this.questions,
            };

            try {
                // Отправка запроса на создание опроса с использованием токена
                const response = await authService.fetchWithToken(`${API_BASE_URL}/forms`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(form),
                });

                // Проверка статуса ответа
                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.message)
                }

                // Получение данных о созданном опросе
                const data = await response.json();
                console.log('Опрос успешно создан:', data);
            } catch (error) {
                console.error('Ошибка при создании опроса:', error);
            }
        },
    },
};
</script>