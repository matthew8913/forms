import { createRouter, createWebHistory } from "vue-router";
import WelcomePage from "../views/WelcomePage.vue";
import Login from "../views/Login.vue";
import Registration from "../views/Registration.vue";
import FormCreation from "@/views/FormCreation.vue";
import FormCompletion from "@/views/FormCompletion.vue";
import { authService } from "../service/authService";

const routes = [
  { path: "/", component: WelcomePage },
  { path: "/login", component: Login },
  { path: "/registration", component: Registration },
  { path: "/create-form", component: FormCreation },
  {
    path: "/complete-form/:formId",
    name: "FormCompletion",
    component: FormCompletion,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach((to, from, next) => {
//   const publicRoutes = ["/", "/login", "/registration", "/create-form", "/complete-form/:formId"];

//   if (!publicRoutes.includes(to.path) && !authService.hasRefreshToken()) {
//     next("/");
//   } else {
//     next();
//   }
// });

export default router;