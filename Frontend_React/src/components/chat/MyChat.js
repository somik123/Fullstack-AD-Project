import React, { useContext, useEffect } from 'react';
import { Avatar } from "@mui/material";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import Fab from '@mui/material/Fab';
import {
  useNavigate,
} from "react-router-dom";
import moment from 'moment'

import { ChatContext } from '../../providers/chatProvider'
import { TabContext } from '../../providers/tabProvider'
import { Claims } from '../../utils/ClientCache'
import GroupDataService from '../../api/group-actions';
import Tooltip from '@mui/material/Tooltip';

const stringToColor = (string) => {
  let hash = 0;
  let i;

  /* eslint-disable no-bitwise */
  for (i = 0; i < string.length; i += 1) {
    hash = string.charCodeAt(i) + ((hash << 5) - hash);
  }

  let color = '#';

  for (i = 0; i < 3; i += 1) {
    const value = (hash >> (i * 8)) & 0xff;
    color += `00${value.toString(16)}`.slice(-2);
  }
  /* eslint-enable no-bitwise */

  return color;
}

export const MyChat = ({ inputSnackBarDetails }) => {
  const { chatContext, dispatch } = useContext(ChatContext);
  const { tabDispatch } = useContext(TabContext);
  let navigate = useNavigate();

  const handleBack = () => {
    tabDispatch({ type: 'SET_INDEX', index: 0 });
    navigate("/")
  }

  useEffect(() => {
    const initChatGroups = () => {
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
          initChatMessages();
        })
        .catch((e) => {
          console.log(e.response);
          inputSnackBarDetails("Something went wrong!", "error");
        });
    };
    const initChatMessages = () => {
      //get each group chat messages
      //dispatch({ type: 'SET_LOADING', loading: false });
    }
    initChatGroups();

    const intervalId = setInterval(() => {  //assign interval to a variable to clear it.
      initChatGroups();
    }, 5000)

    return () => clearInterval(intervalId); //This is important

  }, []);

  return (
    <>
      {
        chatContext.groupList &&
        <div className="conversation-area">
          {
            chatContext.groupList.map((group, index) => (
              <ChatUserComp key={index} group={group} chattingwith={chatContext.chatId} dispatch={dispatch} isPrivate={group.isPrivate} />
            ))
          }
          <div className="navigation">
            <Fab variant="extended" onClick={handleBack}>
              <ArrowBackIcon sx={{ mr: 1 }} />
              Back to main
            </Fab>
          </div>
          <div className="overlay">
          </div>
        </div>
      }
    </>
  );
};

const ChatUserComp = ({ group, chattingwith, dispatch, isPrivate }) => {

  const handleSelect = (chatId, label) => {
    //console.log(group)
    dispatch({
      type: 'SET_CHAT_ID',
      chatId
    });

    if (label == "group") {
      dispatch({
        type: 'SET_GROUP',
        group
      });
      dispatch({
        type: 'SET_IS_GROUPCHAT',
        isGroupChat: true
      });
    }

    dispatch({
      type: 'SET_REFRESHING',
      refreshing: true
    });
  };

  return (
    <>
      {
        <div className={chattingwith == group.id ? "msg active" : "msg"} onClick={() => handleSelect(group.id, "group")}>
          <div className="msg-profile group">
            <Tooltip title={group.name}>
              {isPrivate ? <Avatar sx={{ bgcolor: stringToColor(group.name) }}></Avatar> : <Avatar sx={{ bgcolor: stringToColor(group.name) }}>{group.name.trim().charAt(0)}</Avatar>}
            </Tooltip>
          </div>
          <div className="msg-detail">
            <div className="msg-username">{group.name}</div>
            <div className="msg-content">
              {
                group.lastActiveTime == null && !isPrivate ?
                  <span className="msg-message">Group Owner: {group.creatorName}</span>
                  : <span className="msg-message">Last active</span>
              }
              <span className="msg-date">
                {
                  group.lastActiveTime == null ?
                    moment(group.createDate).fromNow()
                    : moment(group.lastActiveTime).fromNow()
                }
              </span>
            </div>
          </div>
        </div>
      }
    </>
  );
};
