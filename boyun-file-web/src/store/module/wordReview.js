export default {
  state: {
      wordReviewVisible:false,
      wordUrl:''
  },
  mutations: {
      setWordReviewData(state, data) {
          if(data.wordReviewVisible){
            state.wordReviewVisible = data.wordReviewVisible;
            state.wordUrl = data.wordUrl;
          }else{
            state.wordReviewVisible = data.false;
            state.wordUrl = '';
          }
      },
  },
};