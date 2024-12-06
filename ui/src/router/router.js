import { createRouter, createWebHashHistory} from "vue-router";
import Login from "@/views/Login.vue";
import Registration from "@/views/Registration.vue";

export default createRouter({
    history: createWebHashHistory(),
    routes:[
        {
            path: '/login', component: Login
        },
        {
            path: '/register', component: Registration
        }
    ]
})