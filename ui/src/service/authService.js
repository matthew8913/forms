import { API_BASE_URL } from '../../config.js';

export const authService = {
  accessToken: null,
  refreshToken: null,

  setTokens(accessToken, refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  },
//  Метод обновления access токена
  async refreshAccessToken() {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/refresh-token`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ refreshToken: this.refreshToken })
      });

      if (!response.ok) {
        throw new Error('Failed to refresh token');
      }

      const data = await response.json();
      this.setTokens(data.accessToken, data.refreshToken);
      return data.accessToken;
    } catch (error) {
      console.error('Error refreshing token:', error);
      throw error;
    }
  },

//   Метод получения данных с использованием access токена и его обновлением, если он истёк
  async fetchWithToken(url, options = {}) {
    const headers = options.headers || {};
    if (this.accessToken) {
      headers['Authorization'] = `Bearer ${this.accessToken}`;
    }

    try {
      const response = await fetch(url, { ...options, headers });

      if (response.status === 401) {
        const newAccessToken = await this.refreshAccessToken();
        headers['Authorization'] = `Bearer ${newAccessToken}`;
        return fetch(url, { ...options, headers });
      }

      return response;
    } catch (error) {
      console.error('Error fetching with token:', error);
      throw error;
    }
  },

  hasRefreshToken() {
    return !!this.refreshToken;
  }
};