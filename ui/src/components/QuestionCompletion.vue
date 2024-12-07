<template>
    <div class="card mb-3">
        <div class="card-body">
            <h5 class="card-title">{{ index + 1 }}. {{ question.text }}</h5>
            <!-- Числовой тип вопроса -->
            <div v-if="question.type === 'NUMERIC'">
                <input type="number" class="form-control" v-model.number="answer" placeholder="Введите число" />
            </div>
            <!-- Текстовый тип вопроса -->
            <div v-else-if="question.type === 'TEXT'">
                <textarea class="form-control" v-model="answer" placeholder="Введите текст"></textarea>
            </div>
            <!-- Вопрос с одним вариантом -->
            <div v-else-if="question.type === 'SINGLE_CHOICE'">
                <div v-for="option in question.options" :key="option.id" class="form-check">
                    <input class="form-check-input" type="radio" :name="'question' + question.id"
                        :id="'option' + option.id" v-model="answer" :value="option.id" />
                    <label class="form-check-label" :for="'option' + option.id">
                        {{ option.text }}
                    </label>
                </div>
            </div>
            <!-- Вопрос с несколькими вариантами ответов -->
            <div v-else-if="question.type === 'MULTIPLE_CHOICE'">
                <div v-for="option in question.options" :key="option.id" class="form-check">
                    <input class="form-check-input" type="checkbox" :id="'option' + option.id" v-model="answer"
                        :value="option.id" />
                    <label class="form-check-label" :for="'option' + option.id">
                        {{ option.text }}
                    </label>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    props: {
        question: Object,
        index: Number,
    },
    data() {
        return {
            answer: this.question.type === 'MULTIPLE_CHOICE' ? [] : null,
        };
    },
};
</script>
