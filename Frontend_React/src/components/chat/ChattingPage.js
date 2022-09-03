import React, { createRef, useState, useContext, useEffect, useRef } from 'react';
import moment from 'moment'
import LoadingButton from '@mui/lab/LoadingButton';
import { Avatar, Typography, InputBase, IconButton, Link } from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import AttachFileIcon from '@mui/icons-material/AttachFile';
import FilePresentIcon from '@mui/icons-material/FilePresent';
import InsertPhotoOutlinedIcon from '@mui/icons-material/InsertPhotoOutlined';
import VideoCameraBackOutlinedIcon from '@mui/icons-material/VideoCameraBackOutlined';
import PlayCircleIcon from '@mui/icons-material/PlayCircle';
import GifBoxOutlinedIcon from '@mui/icons-material/GifBoxOutlined';
import { green } from '@mui/material/colors';
import { Stack } from '@mui/system';
import InsertEmoticonIcon from '@mui/icons-material/InsertEmoticon';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import data from '@emoji-mart/data'
import Picker from '@emoji-mart/react'
import Tooltip from '@mui/material/Tooltip';

import { ChatlogicStyling } from "./ChatstyleLogic";
import FileUploadDialog from "./FileUploadDialog";
import FullScreenDialog from "./FullScreenDialog";
import GifDialog from "./GifDialog";
import ChatUserProfile from './ChatUserProfile';

import GroupDataService from '../../api/group-actions';
import { ChatContext } from '../../providers/chatProvider'
import { Claims } from '../../utils/ClientCache'

const thumb = {
  display: 'inline-flex',
  borderRadius: 2,
  border: '1px solid #eaeaea',
  marginBottom: 8,
  marginRight: 8,
  padding: 4,
  boxSizing: 'border-box',
  backgroundColor: 'white'
};

const thumbInner = {
  display: 'flex',
};

const img = {
  display: 'flex',
  maxWidth: '500px',
  maxHeight: '300px',
};

export const ChattingPage = ({ inputSnackBarDetails }) => {

  const { chatContext, dispatch } = useContext(ChatContext);
  const scrolldiv = createRef();
  const [open, setOpen] = React.useState(false);
  const [fullScreen, setFullScreen] = React.useState(false);
  const [openGif, setOpenGif] = React.useState(false);
  const [file, setFile] = React.useState('');
  const [fileType, setFileType] = React.useState('image');

  const handleClose = () => {
    setOpen(false);
  };

  const closeFullscreen = () => {
    setFullScreen(false);
  };

  const closeGif = () => {
    setOpenGif(false);
  };

  const colorCode = (message) => {

    if (message.message.includes("[img=") || message.message.includes("[vid=") || message.message.includes("[url=")) {
      if (message.userId == Claims.getUserId())
        return "#F5F7FB";
      else
        return "#BB86FC";
    }
    else {
      switch (message.color) {
        case 1:
          return "#FF3A3A";
        case 2:
          return "#FFC45E";
        case 3:
          return "#5CB2FF";
        case 4:
          return "#E1FF50";
        case 5:
          return "#00FF76";
        default:
          return "#774df2";
      }
    }
  }

  useEffect(() => {
    const scrollToBottom = (node) => {
      node.scrollTop = node.scrollHeight;
    };
    if (chatContext.refreshing) {
      scrollToBottom(scrolldiv.current);
      setTimeout(function () {
        //your code to be executed after 1 second
        dispatch({
          type: 'SET_REFRESHING',
          refreshing: false
        });
      }, 3000);
    }
  });

  useEffect(() => {
    const initMessages = () => {
      if (chatContext.isGroupChat) {
        GroupDataService.getMessages(chatContext.chatId)
          .then((response) => {
            const items = response.data;
            dispatch({
              type: 'SET_MESSAGE_LIST',
              messageList: items
            });
          })
          .catch((e) => {
            console.log(e.response);
            inputSnackBarDetails("Something went wrong!", "error");
          });
      }
    };
    initMessages();

    const intervalId = setInterval(() => {  //assign interval to a variable to clear it.
      initMessages();
    }, 5000)

    return () => clearInterval(intervalId); //This is important
  }, [chatContext.chatId, chatContext.message]);

  useEffect(() => {
    let original = chatContext.messageCount;
    let newCount = chatContext.messageList.length;
    if (newCount > original) {
      dispatch({
        type: 'SET_REFRESHING',
        refreshing: true
      });
    }
    dispatch({
      type: 'SET_MESSAGE_COUNT',
      messageCount: chatContext.messageList.length
    });
  }, [chatContext.messageList]);

  const handleSubmitForAccessChat = (id) => {
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
        }
      })
      .catch((e) => {
        console.log(e.response);
        inputSnackBarDetails('Something went wrong!', 'error');
      });
  };

  return (
    <>
      {
        <div className="chat-area" ref={scrolldiv}>
          <div className="chat-area-header">
            <div className="chat-area-title">{chatContext.group.name}</div>
            <div className="chat-area-group">
              <img className="chat-area-profile" src={chatContext.group.creatorPhoto} alt="" />
              <span>{chatContext.group.participantsId.length - 1}+</span>
            </div>
          </div>
          <div className="chat-area-main">
            {
              chatContext.messageList.map((message) => (
                <div key={message.id}
                  className={
                    message.userId != Claims.getUserId() ? "rihgtuser-chat" : "leftuser-chat"
                  }
                >
                  <div
                    className={message.userId != Claims.getUserId() ? "right-avt" : "left-avt"}
                  >
                    <div className={ChatlogicStyling(message.userId, Claims.getUserId())}
                      style={{ borderBottomColor: colorCode(message) }}>
                      <Typography variant="caption" display="block" gutterBottom style={{ color: '#f5f7fb', fontWeight: 'bold' }}>
                        {message.userName}
                      </Typography>
                      <DisplayMessage message={message.message} setFullScreen={setFullScreen} setFile={setFile}
                        setFileType={setFileType} scrolldiv={scrolldiv} />
                      <p className="time chat-time msg-text">
                        {moment(message.time).fromNow()}
                      </p>
                    </div>
                    {
                      message.userId != Claims.getUserId() ?
                        <Tooltip placement="top" title={
                          <React.Fragment>
                            <div className="msg" onClick={() => handleSubmitForAccessChat(message.userId)}>
                              <ChatUserProfile userPhoto={message.userPhoto}
                                userName={message.userName} inputSnackBarDetails={inputSnackBarDetails} />
                            </div>
                          </React.Fragment>
                        }>
                          <Avatar
                            src={message.userPhoto}
                          />
                        </Tooltip>
                        :
                        <div className="blank-div"></div>
                    }
                  </div>
                </div>
              ))
            }
          </div>
          <ChatFooter dispatch={dispatch} setFileType={setFileType} inputSnackBarDetails={inputSnackBarDetails}
            groupId={chatContext.group?.id} setOpen={setOpen} setOpenGif={setOpenGif} />
        </div>
      }
      <FileUploadDialog open={open} handleClose={handleClose} fileType={fileType} inputSnackBarDetails={inputSnackBarDetails}
        groupId={chatContext.group?.id} dispatch={dispatch} />
      <FullScreenDialog open={fullScreen} handleClose={closeFullscreen}
        file={file} fileType={fileType} />
      <GifDialog open={openGif} handleClose={closeGif} inputSnackBarDetails={inputSnackBarDetails}
        groupId={chatContext.group?.id} dispatch={dispatch} />
    </>
  );
};

function ChatFooter({ dispatch, groupId, setOpen, setFileType, inputSnackBarDetails, setOpenGif }) {
  const [loading, setLoading] = React.useState(false);
  const [message, setMessage] = useState('');
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  const inputElement = useRef();

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
    setTimeout(() => {
      if (inputElement.current) {
        inputElement.current.focus();
        console.log("focused")
      }
    }, 2000)
  };

  const handleImageUpload = (fileType) => {
    setOpen(true);
    setFileType(fileType);
  }

  const onKeyUp = (e) => {
    //console.log(e.keyCode);
    if (e.keyCode == 13) {
      sendMessage();
    }
  }

  const addEmoji = (e) => {
    let emoji = e.native;
    //console.log(emoji);
    setMessage(message.concat(emoji));
    //handleClose();
  }

  const sendMessage = () => {
    if (message.trim().length > 0) {
      let messageRequest = {
        message,
        userId: Claims.getUserId(),
        groupId
      }
      setLoading(true);
      GroupDataService.sendMessage(messageRequest)
        .then((response) => {
          const item = response.data;
          if (item == "Message Saved") {
            setMessage('');
            setLoading(false);
            dispatch({
              type: 'SET_MESSAGE',
              message: messageRequest
            });
          }
        })
        .catch((e) => {
          console.log(e.response);
          setLoading(false);
          inputSnackBarDetails("Something went wrong!", "error");
        });
    }
  }

  return (
    <div className="chat-area-footer">
      <Tooltip title="GIF">
        <IconButton sx={{ p: 1 }} size="large"
          color="info" onClick={() => setOpenGif(true)}>
          <GifBoxOutlinedIcon />
        </IconButton>
      </Tooltip>

      <Tooltip title="emoji">
        <IconButton sx={{ p: 1 }} size="large"
          color="secondary" onClick={handleClick}>
          <InsertEmoticonIcon />
        </IconButton>
      </Tooltip>

      <Tooltip title="image">
        <IconButton sx={{ p: 1 }} size="large" color="success" onClick={() => handleImageUpload("image")}>
          <InsertPhotoOutlinedIcon />
        </IconButton>
      </Tooltip>

      <Tooltip title="video">
        <IconButton sx={{ p: 1 }} size="large" color="default" onClick={() => handleImageUpload("video")}>
          <VideoCameraBackOutlinedIcon />
        </IconButton>
      </Tooltip>

      <Tooltip title="file">
        <IconButton sx={{ p: 1 }} size="large" color="error" onClick={() => handleImageUpload("file")}>
          <AttachFileIcon />
        </IconButton>
      </Tooltip>

      <InputBase autoFocus={true} ref={inputElement} className="chat-input"
        maxRows='3'
        multiline={true}
        sx={{ ml: 1, flex: 1 }}
        value={message}
        placeholder="Type a message" onKeyUp={onKeyUp}
        onChange={(event) => setMessage(event.target.value.length <= 1000 ? event.target.value : event.target.value.slice(0, 1000))}
      />
      <LoadingButton sx={{ p: 1 }}
        onClick={() => sendMessage()}
        endIcon={<SendIcon />}
        loading={loading}
        loadingPosition="end"
        variant='contained'
      >
        Send
      </LoadingButton>
      <Menu
        id="long-menu"
        MenuListProps={{
          'aria-labelledby': 'long-button',
        }}
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        sx={{ ml: 1, mt: -6 }}
      >
        <MenuItem><Picker data={data} onEmojiSelect={addEmoji} /></MenuItem>
      </Menu>
    </div>
  );
}

function DisplayMessage({ message, setFullScreen, setFile, setFileType, scrolldiv }) {
  let value = message;
  let type = "text";
  if (message.includes("[img=")) {
    value = message.substring(5, message.length - 1)
    type = "image"
  }
  else if (message.includes("[url=")) {
    value = message.substring(5, message.length - 1)
    type = "file";
  }
  else if (message.includes("[vid=")) {
    value = message.substring(5, message.length - 1)
    type = "video";
  }

  const handleViewFile = (file, fileType) => {
    setFile(file);
    setFileType(fileType);
    setFullScreen(true);
  }

  const onLoad = () => {
    // Revoke data uri after image is loaded
    URL.revokeObjectURL(value)
    const scrollToBottom = (node) => {
      node.scrollTop = node.scrollHeight;
    };
    scrollToBottom(scrolldiv.current);
    //console.log("image loaded")
  }

  return (
    <>
      {
        type == "text" ?
          <p className='msg-text'>{value}</p>
          :
          type == "image" ?
            <Link onClick={() => handleViewFile(value, "image")}>
              <div style={thumb}>
                <div style={thumbInner}>
                  <img
                    src={value}
                    style={img}
                    onLoad={onLoad}
                  />
                </div>
              </div>
            </Link>
            : type == "file" ?
              <Stack>
                <Link href={value} underline="hover" target="_blank">
                  <Avatar sx={{ bgcolor: green, width: 90, height: 90 }} variant="rounded">
                    <FilePresentIcon sx={{ width: 60, height: 60 }} />
                  </Avatar>
                  <Typography sx={{ overflow: 'hidden', maxWidth: 100 }} variant="subtitle1" noWrap>
                    View file
                  </Typography>
                </Link>
              </Stack>
              : <Link onClick={() => handleViewFile(value, "video")}>
                <Avatar sx={{ bgcolor: green, width: 90, height: 90 }} variant="rounded">
                  <PlayCircleIcon sx={{ width: 60, height: 60 }} />
                </Avatar>
                <Typography sx={{ overflow: 'hidden', maxWidth: 100 }} variant="subtitle1" noWrap>
                  Play video
                </Typography>
              </Link>
      }
    </>
  );
}


