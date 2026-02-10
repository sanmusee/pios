import request from '@/utils/request'

export function getHealth() {
  return request({
    url: '/v1/system/health',
    method: 'get'
  })
}

export function getSystemInfo() {
  return request({
    url: '/v1/system/info',
    method: 'get'
  })
}
