import React, { createRef, useState, useContext, useEffect } from 'react';
import LoadingButton from '@mui/lab/LoadingButton';
import { Avatar, Typography, Paper, InputBase, IconButton, Link, SpeedDialIcon, Container } from "@mui/material";
import SendIcon from "@mui/icons-material/Send";
import AttachFileIcon from '@mui/icons-material/AttachFile';
import FilePresentIcon from '@mui/icons-material/FilePresent';
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate';
import VideoFileIcon from '@mui/icons-material/VideoFile';
import PlayCircleIcon from '@mui/icons-material/PlayCircle';
import Box from '@mui/material/Box';
import SpeedDial from '@mui/material/SpeedDial';
import SpeedDialAction from '@mui/material/SpeedDialAction';
import { green } from '@mui/material/colors';
import { Stack } from '@mui/system';
import Button from '@mui/material/Button';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import Fab from '@mui/material/Fab';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import FavoriteIcon from '@mui/icons-material/Favorite';
import NavigationIcon from '@mui/icons-material/Navigation';
import {
    useNavigate,
} from "react-router-dom";
import { TabContext } from '../providers/tabProvider'

const Protected = () => {
    const { tabDispatch } = useContext(TabContext);
    document.body.style.overflowY = 'hidden';
    window.scrollTo(0, 0);
    let navigate = useNavigate();
    const scrolldiv = createRef();

    useEffect(() => {
        const scrollToBottom = (node) => {
            node.scrollTop = node.scrollHeight;
        };
        scrollToBottom(scrolldiv.current);
    });

    const handleBack = () => {
        tabDispatch({ type: 'SET_INDEX', index: 0 });
        navigate("/")
    }

    return (
        <div style={{ marginTop: "-48px" }}>
            <div class="app" >
                <div class="header">
                    <div class="logo">
                        <img src="/logo.png"/>
                    </div>
                    <div className="user-header">
                        <p className="user-name">Study Buddy</p>
                    </div>
                    <div class="search-bar">
                        <input type="text" placeholder="Search..." />
                    </div>
                    <div class="user-settings">
                        <Avatar></Avatar>
                        <p className="user-name">Amber</p>
                    </div>
                </div>
                <div class="wrapper">
                    <div class="conversation-area">
                        <div class="msg active">
                            <div class="msg-profile group">
                                <svg viewBox="0 0 24 24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round" class="css-i6dzq1">
                                    <path d="M12 2l10 6.5v7L12 22 2 15.5v-7L12 2zM12 22v-6.5" />
                                    <path d="M22 8.5l-10 7-10-7" />
                                    <path d="M2 15.5l10-7 10 7M12 2v6.5" /></svg>
                            </div>
                            <div class="msg-detail">
                                <div class="msg-username">CodePen Group</div>
                                <div class="msg-content">
                                    <span class="msg-message">Aysenur: I love CSS</span>
                                    <span class="msg-date">28m</span>
                                </div>
                            </div>
                        </div>
                        <div class="msg">
                            <img class="msg-profile" src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3364143/download+%284%29+%281%29.png" alt="" />
                            <div class="msg-detail">
                                <div class="msg-username">Jared Jackson</div>
                                <div class="msg-content">
                                    <span class="msg-message">Tattooed brooklyn typewriter gastropub</span>
                                    <span class="msg-date">18m</span>
                                </div>
                            </div>
                        </div>
                        <div class="msg online">
                            <img class="msg-profile" src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3364143/download+%283%29+%281%29.png" alt="" />
                            <div class="msg-detail">
                                <div class="msg-username">Henry Clark</div>
                                <div class="msg-content">
                                    <span class="msg-message">Ethical typewriter williamsburg lo-fi street art</span>
                                    <span class="msg-date">2h</span>
                                </div>
                            </div>
                        </div>
                        <div class="navigation">
                            <Fab variant="extended" onClick={handleBack}>
                                <ArrowBackIcon sx={{ mr: 1 }} />
                                Back to main
                            </Fab>
                        </div>
                        <div class="overlay">
                        </div>
                    </div>
                    <div class="chat-area" ref={scrolldiv}>
                        <div class="chat-area-header">
                            <div class="chat-area-title">CodePen Group</div>
                            <div class="chat-area-group">
                                <img class="chat-area-profile" src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3364143/download+%283%29+%281%29.png" alt="" />
                                <img class="chat-area-profile" src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3364143/download+%282%29.png" alt="" />
                                <img class="chat-area-profile" src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3364143/download+%2812%29.png" alt="" />
                                <span>+4</span>
                            </div>
                        </div>
                        <div class="chat-area-main">

                            <div class="chat-msg">
                                <div class="chat-msg-profile">
                                    <img class="chat-msg-img" src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3364143/download+%282%29.png" alt="" />
                                    <div class="chat-msg-date">Message seen 2.45pm</div>
                                </div>
                                <div class="chat-msg-content">
                                    <div class="chat-msg-text">Aenean tristique maximus tortor non tincidunt. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae😊</div>
                                    <div class="chat-msg-text">Ut faucibus pulvinar elementum integer enim neque volutpat.</div>
                                </div>
                            </div>
                            <div class="chat-msg owner">
                                <div class="chat-msg-profile">
                                    <img class="chat-msg-img" src="https://s3-us-west-2.amazonaws.com/s.cdpn.io/3364143/download+%281%29.png" alt="" />
                                    <div class="chat-msg-date">Message seen 2.50pm</div>
                                </div>
                                <div class="chat-msg-content">
                                    <div class="chat-msg-text">posuere eget augue sodales, aliquet posuere eros.</div>
                                    <div class="chat-msg-text">Cras mollis nec arcu malesuada tincidunt.</div>
                                </div>
                            </div>
                        </div>
                        <div class="chat-area-footer">
                            <IconButton sx={{ p: '10px' }} size="large" color="primary">
                                <AddPhotoAlternateIcon />
                            </IconButton>
                            <IconButton sx={{ p: '10px' }} size="large" color="secondary">
                                <VideoFileIcon />
                            </IconButton>
                            <IconButton sx={{ p: '10px' }} size="large" color="error">
                                <AttachFileIcon />
                            </IconButton>
                            <InputBase
                                maxRows='3'
                                multiline={true}
                                sx={{ ml: 1, flex: 1 }}
                                placeholder="Type a message"
                            />
                            <LoadingButton sx={{ p: 1 }}
                                endIcon={<SendIcon />}
                                loadingPosition="end"
                                variant='contained'
                            >
                                Send
                            </LoadingButton>
                        </div>
                    </div>
                </div>
            </div>
        </div >
    )
}

export default Protected;