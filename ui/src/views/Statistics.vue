<template>
    <div class="container mt-5">
        <h1 class="text-center mb-4">Статистика</h1>
        <div v-if="statistic">
            <div class="mb-4">
                <h3>Общее количество завершений: {{ statistic.numberOfCompletions }}</h3>
            </div>
            <div v-for="(questionStat, index) in statistic.questionStatistic" :key="index" class="mb-4">
                <h4>Вопрос {{ index + 1 }}: {{ questionStat.questionText }}</h4>
                <div v-if="questionStat.statistic.answers && questionStat.statistic.minAnswer !== undefined">
                    <h5>Числовая статистика</h5>
                    <p>Минимум: {{ questionStat.statistic.minAnswer }}</p>
                    <p>Максимум: {{ questionStat.statistic.maxAnswer }}</p>
                    <p>Среднее: {{ questionStat.statistic.avgAnswer }}</p>
                    <p>Ответы: {{ questionStat.statistic.answers.join(', ') }}</p>
                </div>
                <div v-else-if="questionStat.statistic.percentageOfAnswered">
                    <h5>Статистика выбора</h5>
                    <ul>
                        <li v-for="(answer, idx) in questionStat.statistic.answers" :key="idx">
                            {{ answer }} - {{ questionStat.statistic.numberOfAnswered[idx] }} ответов ({{
                                questionStat.statistic.percentageOfAnswered[idx] }}%)
                        </li>
                    </ul>
                </div>
                <div v-else>
                    <p>Нет данных для этого вопроса.</p>
                </div>
            </div>
        </div>
        <div v-else class="text-center">
            <p>Загрузка статистики...</p>
        </div>
    </div>
</template>

<script>
import { API_BASE_URL } from '../../config.js';
import { authService } from '@/service/authService.js';

export default {
    data() {
        return {
            statistic: null,
        };
    },
    async created() {
        const formId = this.$route.params.formId; 
        await this.fetchStatistics(formId);
    },
    methods: {
        async fetchStatistics(formId) {
            try {
                const response = await authService.fetchWithToken(`${API_BASE_URL}/statistic/${formId}`);
                if (!response.ok) {
                    const errorData = await response.json();
                    console.error('Ошибка при загрузке статистики:', errorData);
                    return;
                }
                const data = await response.json();
                console.log(data)

                this.statistic = data;
            } catch (error) {
                console.error('Ошибка при загрузке статистики:', error);
            }
        },
    },
};
</script>
