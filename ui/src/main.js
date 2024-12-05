import { createApp } from 'vue'
import App from './App.vue' // Импорт главного компонента приложения
import router from './router/router'


import 'bootstrap/dist/css/bootstrap.css' // Импорт стилей Bootstrap
import 'bootstrap/dist/js/bootstrap.bundle.js' // Импорт скриптов Bootstrap

const app = createApp(App); // Создание экземпляра приложения
app.use(router)

app.mount('#app'); // Монтирование приложения в элемент с id="app"