import request from '@/utils/request'

export function getInfoSignals() {
  return request({
    url: '/v1/info-signals',
    method: 'get'
  })
}

export function getInfoSignal(id) {
  return request({
    url: `/v1/info-signals/${id}`,
    method: 'get'
  })
}

export function createInfoSignal(data) {
  return request({
    url: '/v1/info-signals',
    method: 'post',
    data
  })
}

export function updateInfoSignal(id, data) {
  return request({
    url: `/v1/info-signals/${id}`,
    method: 'put',
    data
  })
}

export function deleteInfoSignal(id) {
  return request({
    url: `/v1/info-signals/${id}`,
    method: 'delete'
  })
}
