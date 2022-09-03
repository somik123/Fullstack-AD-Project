export const initialState = {
    loading: false,
    groupList: [],
    filterGroupList: [],
    group: null
};

export const groupReducer = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_LOADING':
            return { ...state, loading: action.loading };
        case 'SET_GROUP_LIST':
            return { ...state, groupList: action.groupList };
        case 'SET_FILTER_GROUP':
            return { ...state, filterGroupList: action.filterGroupList };
        case 'SET_GROUP':
            return { ...state, group: action.group };
        default:
            return state;
    }
}