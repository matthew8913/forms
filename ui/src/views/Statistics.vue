<template>
    <div class="container mt-5">
        <h1 class="text-center mb-4">Статистика</h1>
        <div v-if="statistic" class="row">
            <div class="col-12 mb-4">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h3 class="card-title">Пользователей, прошедших опрос: {{ statistic.numberOfCompletions }}</h3>
                    </div>
                </div>
            </div>

            <div v-for="(questionStat, index) in statistic.questionStatistic" :key="index" class="col-12 mb-4">
                <div class="card shadow-sm">
                    <div class="card-body">
                        <h4 class="card-title">Вопрос {{ index + 1 }}: {{ questionStat.questionText }}</h4>

                        <div v-if="questionStat.questionType === 'TEXT'" class="mt-3">
                            <h5 class="text-success">Текстовые ответы</h5>
                            <div v-if="!showFullTextAnswers[index]" class="overflow-hidden" style="max-height: 100px;">
                                <ul class="list-unstyled">
                                    <li v-for="(answer, idx) in questionStat.statistic" :key="idx">
                                        {{ answer }}
                                    </li>
                                </ul>
                            </div>
                            <button class="btn btn-link p-0" @click="toggleTextAnswers(index)">
                                {{ showFullTextAnswers[index] ? 'Скрыть' : 'Показать все' }}
                            </button>
                            <div v-if="showFullTextAnswers[index]" class="mt-2">
                                <ul class="list-unstyled">
                                    <li v-for="(answer, idx) in questionStat.statistic" :key="idx">
                                        {{ answer }}
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <div v-else-if="questionStat.questionType === 'NUMERIC'" class="mt-3">
                            <h5 class="text-success">Числовая статистика</h5>
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Минимум:</strong> {{ questionStat.statistic.minAnswer }}</p>
                                    <p><strong>Максимум:</strong> {{ questionStat.statistic.maxAnswer }}</p>
                                    <p><strong>Среднее:</strong> {{ questionStat.statistic.avgAnswer }}</p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Ответы:</strong></p>
                                    <div v-if="!showFullAnswers[index]" class="overflow-hidden" style="max-height: 50px;">
                                        <span>{{ formattedAnswers(questionStat.statistic.answers) }}</span>
                                    </div>
                                    <button class="btn btn-link p-0" @click="toggleAnswers(index)">
                                        {{ showFullAnswers[index] ? 'Скрыть' : 'Показать все' }}
                                    </button>
                                    <div v-if="showFullAnswers[index]" class="mt-2">
                                        <ul class="list-unstyled">
                                            <li v-for="(answer, idx) in questionStat.statistic.answers" :key="idx">
                                                {{ answer }}
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div v-else-if="questionStat.questionType === 'SINGLE_CHOICE' || questionStat.questionType === 'MULTIPLE_CHOICE'"
                            class="mt-3">
                            <h5 class="text-success">Статистика выбранных вариантов</h5>
                            <table class="table table-bordered table-hover">
                                <thead class="thead-light">
                                    <tr>
                                        <th>Ответ</th>
                                        <th>Количество ответов</th>
                                        <th>Процент</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(answer, idx) in questionStat.statistic.answers" :key="idx">
                                        <td>{{ answer }}</td>
                                        <td>{{ questionStat.statistic.numberOfAnswered[idx] }}</td>
                                        <td>
                                            <div class="progress">
                                                <div class="progress-bar" role="progressbar"
                                                    :style="{ width: questionStat.statistic.percentageOfAnswered[idx] + '%' }"
                                                    :aria-valuenow="questionStat.statistic.percentageOfAnswered[idx]"
                                                    aria-valuemin="0" aria-valuemax="100">
                                                    {{ questionStat.statistic.percentageOfAnswered[idx] }}%
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>


                      <div v-else-if="questionStat.questionType === 'RATING'" class="mt-3">
                        <h5 class="text-success">Рейтинговая статистика</h5>
                        <table class="table table-bordered table-hover">
                          <thead class="thead-light">
                          <tr>
                            <th>Позиция</th>
                            <th>Вариант</th>
                          </tr>
                          </thead>
                          <tbody>
                          <tr v-for="(options, position) in questionStat.statistic" :key="position">
                            <td>{{ position }}</td>
                            <td>
                              <ul class="list-unstyled">
                                <li v-for="(option, optionIdx) in options" :key="optionIdx">
                                  {{ option }}
                                </li>
                              </ul>
                            </td>
                          </tr>
                          </tbody>
                        </table>
                      </div>


                        <div v-else class="mt-3">
                            <p class="text-muted">Нет данных для этого вопроса.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div v-else class="text-center">
            <p>Загрузка статистики...</p>
        </div>
        <div class="d-flex justify-content-center mt-4">
            <button @click="goBack" class="btn btn-secondary">Назад</button>
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
            showFullAnswers: {}, 
            showFullTextAnswers: {}, 
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
                console.log(data);

                this.statistic = data;
            } catch (error) {
                console.error('Ошибка при загрузке статистики:', error);
            }
        },
        formattedAnswers(answers) {
            const sortedAnswers = answers.sort((a, b) => a - b);
            if (sortedAnswers.length > 5) {
                return sortedAnswers.slice(0, 5).join(', ') + '...';
            }
            return sortedAnswers.join(', ');
        },
        toggleAnswers(index) {
            this.showFullAnswers[index] = !this.showFullAnswers[index];
        },
        toggleTextAnswers(index) {
            this.showFullTextAnswers[index] = !this.showFullTextAnswers[index];
        },
        goBack() {
            this.$router.push('/form-list');
        }
    },
};
</script>