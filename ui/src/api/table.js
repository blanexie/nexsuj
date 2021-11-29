import request from '@/utils/request'

import store from "@/store";

export function getList(params) {
  return request({
    url: '/torrent/list',
    method: 'post',
    data: params
  })
}
