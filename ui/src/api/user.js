import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'get',
    // params: { token },
    headers:{"Authorization" :"Bearer "+token }
  })
}

export function logout() {
  return request({
    url: '/user/logout',
    method: 'post'
  })
}
