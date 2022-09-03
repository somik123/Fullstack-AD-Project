export const initialState = {
    loading: false,
    snackBarDetails: {
        open: false,
        message: '',
        type: 'error',
    },
    refreshing: false,
    messageList: [],
    message: null,
    groupList: [],
    filterGroupList: [],
    chatHistory: [],
    group: { name: '', isPrivate: false, id: 0, desciption: '', creatorName: '' },
    user: { username: '', id: 0, email: '', name: '' },
    chatId: 0,
    messageCount: 0,
    isGroupChat: false
};

export const chatReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_LOADING':
            return { ...state, loading: action.loading };
        case 'SET_REFRESHING':
            return { ...state, refreshing: action.refreshing };
        case 'SET_SNACKBARDETAILS':
            return { ...state, snackBarDetails: action.snackBarDetails };
        case 'SET_MESSAGE_LIST':
            return { ...state, messageList: action.messageList };
        case 'SET_MESSAGE':
            return { ...state, message: action.message };
        case 'SET_GROUP_LIST':
            return { ...state, groupList: action.groupList };
        case 'SET_FILTER_GROUP':
            return { ...state, filterGroupList: action.filterGroupList };
        case 'SET_GROUP':
            return { ...state, group: action.group };
        case 'SET_USER':
            return { ...state, user: action.user };
        case 'SET_CHAT_ID':
            return { ...state, chatId: action.chatId };
        case 'SET_MESSAGE_COUNT':
            return { ...state, messageCount: action.messageCount };
        case 'SET_CHAT_HISTORY':
            return { ...state, chatHistory: action.chatHistory };
        case 'SET_IS_GROUPCHAT':
            return { ...state, isGroupChat: action.isGroupChat };
        default:
            return state;
    }
}