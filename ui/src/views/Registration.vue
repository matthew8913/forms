<template>
    <div class="container mt-5">
      <div class="row justify-content-center">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header">
              <h3 class="text-center">Registration</h3>
            </div>
            <div class="card-body">
              <form @submit.prevent="handleRegistration">
                <div class="mb-3">
                  <label for="username" class="form-label">Username</label>
                  <input type="text" class="form-control" id="username" v-model="username" placeholder="ivanov123" required>
                </div>
                <div class="mb-3">
                  <label for="email" class="form-label">Email</label>
                  <input type="email" class="form-control" id="email" v-model="email" placeholder="ivanov@example.com" required>
                </div>
                <div class="mb-3">
                  <label for="password" class="form-label">Password</label>
                  <input type="password" class="form-control" id="password" v-model="password" required>
                </div>
                <div class="mb-3">
                  <label for="confirmPassword" class="form-label">Confirm Password</label>
                  <input type="password" class="form-control" id="confirmPassword" v-model="confirmPassword" required>
                </div>
                <button type="submit" class="btn btn-primary">Register</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Registration',
    data() {
      return {
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        message: ''
      };
    },
    methods: {
      handleRegistration() {
        if (this.password !== this.confirmPassword) {
          this.message = 'Passwords do not match';
          return;
        }
  
        console.log('Username:', this.username);
        console.log('Email:', this.email);
        console.log('Password:', this.password);
        console.log('Message:', this.message);
  
        // Здесь можно добавить логику для отправки данных на сервер
      }
    }
  }
  </script>
  
  <style scoped>
  .card {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  </style>