import React, { useReducer, useEffect, useRef, useState, useContext } from 'react'
import {
  Avatar, Container,
  Box, useMediaQuery, Snackbar,
  Alert,
  Typography
} from "@mui/material";
import {
  useParams,
  useNavigate
} from "react-router-dom";
import ChatOutlinedIcon from '@mui/icons-material/ChatOutlined';
import Dialog from '@mui/material/Dialog';
import DialogContent from '@mui/material/DialogContent';
import Modal from '@mui/material/Modal';
import Slide from '@mui/material/Slide';
import { pink } from '@mui/material/colors';
import LinearProgress from '@mui/material/LinearProgress';

import { Claims } from '../../utils/ClientCache'
import { ChattingPage } from "./ChattingPage";
import { MyChat } from "./MyChat";
import Loading from './../shared/Loading';

import GroupDataService from '../../api/group-actions';
import AccountDataService from '../../api/account-actions';
import { chatReducer, initialState } from '../../reducers/chatReducer'
import { TabContext } from '../../providers/tabProvider'
import { ChatProvider } from '../../providers/chatProvider'

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: "auto",
  height: '100%',
  border: '2px solid #000',
  boxShadow: 24,
  display: 'flex',
  flexGrow: 1,
  overflow: 'hidden',
};

export const ChatHome = () => {
  document.body.style.overflowY = 'hidden';
  window.scrollTo(0, 0);

  let navigate = useNavigate();
  const [chatContext, dispatch] = useReducer(chatReducer, initialState);
  const { tabDispatch } = useContext(TabContext);
  let { id } = useParams();
  const [open, setOpen] = React.useState(false);
  const hidden = useMediaQuery(theme => theme.breakpoints.up('sm'));
  const [search, setSearch] = useState(false);
  const [searching, setSearching] = useState(false);
  const [search_result, setSearch_result] = useState([]);
  const ref = useRef();
  const inputElement = useRef(null);

  const goToAccount = () => {
    tabDispatch({ type: 'SET_INDEX', index: 2 });
    navigate("/account")
  }

  const goToMain = () => {
    tabDispatch({ type: 'SET_INDEX', index: 0 });
    navigate("/")
  }

  const openSearchDialog = () => {
    setSearch(true);
    setTimeout(() => {
      if (inputElement.current) {
        inputElement.current.focus();
        //console.log("focused")
      }
    }, 1000)
  }

  const handleSnackBarClose = () => {
    chatContext.snackBarDetails.open = false;
    dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: chatContext.snackBarDetails });
  };

  const inputSnackBarDetails = (message, type) => {
    chatContext.snackBarDetails = {
      open: true,
      message,
      type
    };
    dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: chatContext.snackBarDetails });
  }

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSearchClose = () => {
    setSearch(false);
    setSearch_result([]);
  }

  const handleQuery = (e) => {
    return function (e) {
      if (!e.target.value) {
        setSearch_result([]);
        return;
      }

      if (ref.current) clearTimeout(ref.current);
      setSearch(true);
      setSearching(true);

      ref.current = setTimeout(() => {
        if (e.target.value.length > 0) {

          AccountDataService.searchUser(e.target.value)
            .then((response) => {
              const items = response.data;
              setSearch_result(items);
              setSearching(false);
            })
            .catch((error) => {
              console.log(error);
              inputSnackBarDetails("Something went wrong!","error");
            });
        }
      }, 1000);
    };
  };

  useEffect(() => {
    const initChats = () => {
      dispatch({ type: 'SET_LOADING', loading: true });
      GroupDataService.getGroups()
        .then((response) => {
          const items = response.data;
          const groupList = (items).map((item) => {
            item.name = item.isPrivate ?
              item.name.split("|")[0].trim() == Claims.getUserName() ? item.name.split("|")[1] : item.name.split("|")[0]
              : item.name;
            return item;
          });
          dispatch({
            type: 'SET_GROUP_LIST',
            groupList
          });
          if (id != undefined) {
            let group = groupList.filter(t => t.id == id)[0];
            dispatch({
              type: 'SET_GROUP',
              group
            });
            dispatch({
              type: 'SET_CHAT_ID',
              chatId: id
            });
            dispatch({
              type: 'SET_IS_GROUPCHAT',
              isGroupChat: true
            });
          }
          initChatMessages();
        })
        .catch((e) => {
          console.log(e.response);
          inputSnackBarDetails("Something went wrong!", "error");
        });
    };
    const initChatMessages = () => {
      //get each group chat messages
      dispatch({ type: 'SET_LOADING', loading: false });
    }
    initChats();
  }, []);

  if (chatContext.loading) return <div style={{ marginBottom: '20px' }}><Container> <Loading /></Container></div>;

  return (
    <div style={{ marginTop: '-48px' }}>
      <ChatProvider value={{ chatContext, dispatch }}>
        <div className="app" >
          <div className="header">
            <div className="logo" style={{ cursor: 'pointer' }} onClick={goToMain}>
              <img src="/logo.png" style={{ width: 45, height: 45 }} />
            </div>
            <div className="user-header">
              <p className="user-name">Study Buddy</p>
            </div>
            <div className="search-bar">
              <input readOnly type="text" onClick={openSearchDialog} placeholder="Search..." />
            </div>
            <div className="user-settings">
              <div className="dark-light" onClick={() => document.body.classList.toggle('dark-mode')}>
                <svg viewBox="0 0 24 24" stroke="currentColor" fill="none">
                  <path d="M21 12.79A9 9 0 1111.21 3 7 7 0 0021 12.79z" /></svg>
              </div>
              <p className="user-name">{Claims.getUserName()}</p>
              {
                hidden ? <Avatar src={Claims.getProfileImage()} onClick={goToAccount} />
                  : <Avatar size="small" sx={{ bgcolor: pink[500] }} onClick={handleClickOpen}>
                    <ChatOutlinedIcon />
                  </Avatar>
              }
            </div>
          </div>

          <div className="wrapper">
            {hidden ? <MyChat inputSnackBarDetails={inputSnackBarDetails} /> : null}
            {chatContext.chatId == 0 ? <MessageStarter /> : <ChattingPage inputSnackBarDetails={inputSnackBarDetails} />}
          </div>

          <Modal
            open={open}
            onClose={handleClose}
          >
            <Box sx={style} className="chat-dialog">
              <div className="wrapper">
                <MyChat inputSnackBarDetails={inputSnackBarDetails} />
              </div>
            </Box>
          </Modal>

          <Dialog
            fullWidth={true}
            maxWidth="sm"
            disableEscapeKeyDown={true}
            open={search}
            TransitionComponent={Transition}
            onClose={handleSearchClose}
          >
            <DialogContent>
              <div>
                <div className="search-bar-dialog">
                  <input ref={inputElement} onChange={handleQuery()}
                    placeholder="Search users..."
                  />
                </div>

              </div>
              {
                !searching ?
                  search_result.length > 0 ?
                    search_result.map((el) => (
                      <SearchUserComp
                        key={el.id}
                        {...el}
                        setSearch={setSearch}
                        setSearch_result={setSearch_result}
                        dispatch={dispatch}
                        chatContext={chatContext}
                        inputSnackBarDetails={inputSnackBarDetails}
                      />
                    ))
                    :
                    <Typography variant='subtitle2' sx={{ textAlign: 'center' }}>No user found...</Typography>
                  :
                  <Box sx={{ width: '100%' }}>
                    <LinearProgress />
                  </Box>

              }
            </DialogContent>
          </Dialog>

          <Snackbar
            anchorOrigin={{ horizontal: 'center', vertical: 'bottom' }}
            key={`bottom, center`}
            open={chatContext.snackBarDetails.open}
            autoHideDuration={6000}
            onClose={handleSnackBarClose}>
            <Alert onClose={handleSnackBarClose} severity={chatContext.snackBarDetails.type}>
              {chatContext.snackBarDetails.message}
            </Alert>
          </Snackbar>

        </div>
      </ChatProvider >
    </div >
  );
};

const MessageStarter = () => {
  return (
    <div className="start-msg">
      <div>
        <Avatar src={Claims.getProfileImage()} sx={{ width: 70, height: 70 }} />
        <h3 className='chat-input'>Welcome, {Claims.getDisplayName()}</h3>
        <p className='chat-input'>Please select a chat to start messaging.</p>
      </div>
    </div>
  );
};

export const SearchUserComp = ({ id,
  email,
  name,
  username,
  photo,
  setSearch,
  setSearch_result,
  dispatch,
  chatContext,
  inputSnackBarDetails }) => {

  const handleSubmitForAccessChat = () => {
    GroupDataService.createPrivateGroup(Claims.getUserId(), id)
      .then((response) => {
        const group = response.data;
        if (group.id == undefined) inputSnackBarDetails(group, 'error');
        else {
          group.name = group.name.split("|")[0].trim() == Claims.getUserName() ? group.name.split("|")[1] : group.name.split("|")[0];
          let groupList = chatContext.groupList;
          let existing = groupList.filter(t => t.id == group.id)[0];
          if (existing == undefined) {
            //new
            groupList.push(group);
            dispatch({
              type: 'SET_GROUP_LIST',
              groupList
            });
            dispatch({
              type: 'SET_CHAT_ID',
              chatId: group.id
            });
            dispatch({
              type: 'SET_GROUP',
              group
            });
          }
          else {
            //existing
            dispatch({
              type: 'SET_CHAT_ID',
              chatId: group.id
            });
            dispatch({
              type: 'SET_GROUP',
              group
            });
          }
          setSearch_result([]);
          setSearch(false);
        }
      })
      .catch((e) => {
        console.log(e.response);
        inputSnackBarDetails('Something went wrong!', 'error');
      });
  };

  return (
    <div onClick={handleSubmitForAccessChat} className="user">
      <div className="history-cont">
        <div>{<Avatar src={photo} />}</div>
        <div>
          <p className="name">{name}({username})</p>
          <p className="chat">{email}</p>
        </div>
      </div>
    </div>
  );
};