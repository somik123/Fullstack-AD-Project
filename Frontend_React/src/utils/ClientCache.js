export const Claims = (() => {
    const getToken = () => localStorage.getItem('jwt');
    const setToken = (key) => localStorage.setItem('jwt', key);

    const getUserName = () => localStorage.getItem('userName');
    const setUserName = (key) => localStorage.setItem('userName', key);

    const getUserId = () => localStorage.getItem('userId');
    const setUserId = (key) => localStorage.setItem('userId', key);

    const getDisplayName = () => localStorage.getItem('displayName');
    const setDisplayName = (key) => localStorage.setItem('displayName', key);

    const getEmail = () => localStorage.getItem('email');
    const setEmail = (key) => localStorage.setItem('email', key);

    const getProfileImage = () => localStorage.getItem('profileImage');
    const setProfileImage = (key) => localStorage.setItem('profileImage', key);

    const clearAll = ()=> localStorage.clear();

    return {
        getUserName: getUserName,
        setUserName: setUserName,
        getUserId:getUserId,
        setUserId:setUserId,
        getDisplayName:getDisplayName,
        setDisplayName:setDisplayName,
        getToken: getToken,
        setToken: setToken,
        getEmail: getEmail,
        setEmail: setEmail,
        getProfileImage:getProfileImage,
        setProfileImage:setProfileImage,
        clearAll:clearAll
    }
})();