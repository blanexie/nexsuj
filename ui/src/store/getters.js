const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  role: state => state.user.role,
  user: state => state.user.userInfo,
  avatar: state => state.user.userInfo.avatar
}
export default getters
