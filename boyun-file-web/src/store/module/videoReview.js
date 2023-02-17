export default {
  state: {
      videoReviewVisible: false, //  视频查看组件显隐状态
      videoUrl: '', //  视频链接
  },
  mutations: {
      setVideoReviewData(state, data) {
          if (data.videoReviewVisible) {
              state.videoReviewVisible = data.videoReviewVisible;
              state.videoUrl = data.videoUrl;
          } else {
              state.videoReviewVisible = data.false;
              state.videoUrl = '';
          }
      },
  },
};