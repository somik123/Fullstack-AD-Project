import React, { useEffect, useState } from 'react';
import { Divider, List, ListItem, ListItemIcon, ListItemText } from '@mui/material';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';
import Paper from '@mui/material/Paper';
import { useTheme } from '@mui/material/styles';
import { useDropzone } from 'react-dropzone';
import FilePresentIcon from '@mui/icons-material/FilePresent';
import LoadingButton from '@mui/lab/LoadingButton';
import SendIcon from '@mui/icons-material/Send';

import FileDataService from '../../api/file-actions';
import GroupDataService from '../../api/group-actions';
import ChatDataService from '../../api/chat-actions';
import { Claims } from '../../utils/ClientCache'

const thumbsContainer = {
    display: 'flex',
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 16
};

const thumb = {
    display: 'inline-flex',
    borderRadius: 2,
    border: '1px solid #eaeaea',
    marginBottom: 8,
    marginRight: 8,
    width: 100,
    height: 100,
    padding: 4,
    boxSizing: 'border-box'
};

const thumbInner = {
    display: 'flex',
    minWidth: 0,
    overflow: 'hidden'
};

const img = {
    display: 'block',
    width: 'auto',
    height: '100%'
};

export default function FileUploadDialog({ open, handleClose, fileType, dispatch, groupId, userId, inputSnackBarDetails }) {
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));
    const [files, setFiles] = useState([]);
    const [caption, setCaption] = useState('');
    const [loading, setLoading] = React.useState(false);
    const { getRootProps, getInputProps } = fileType == "image" ? useDropzone({
        maxFiles: 2,
        accept: {
            'image/*': []
        },
        onDrop: acceptedFiles => {
            setFiles(acceptedFiles.map(file => Object.assign(file, {
                preview: URL.createObjectURL(file)
            })));
        }
    })
        : fileType == "file" ?
            useDropzone({
                maxFiles: 2,
                accept: {
                    'text/*': ['.html', '.htm', '.xlsx', '.xls', '.doc', '.docx', '.ppt', '.pptx', '.txt', '.pdf', '.zip'],
                },
                onDrop: acceptedFiles => {
                    setFiles(acceptedFiles);
                }
            }) :
            useDropzone({
                maxFiles: 2,
                accept: {
                    'video/*': []
                },
                onDrop: acceptedFiles => {
                    setFiles(acceptedFiles);
                }
            });

    useEffect(() => {
        // Make sure to revoke the data uris to avoid memory leaks, will run on unmount
        return () => files.forEach(file => URL.revokeObjectURL(file.preview));
    }, []);

    const thumbs = files.map(file => (
        <div style={thumb} key={file.name}>
            <div style={thumbInner}>
                <img
                    src={file.preview}
                    style={img}
                    // Revoke data uri after image is loaded
                    onLoad={() => { URL.revokeObjectURL(file.preview) }}
                />
            </div>
        </div>
    ));

    const handelCancel = () => {
        console.log(groupId + ", " + userId);
        setFiles([]);
        setCaption('');
        handleClose();
    }

    const handleChange = (event) => {
        setCaption(event.target.value);
    };

    const handleFileSend = () => {
        if (files.length > 0) {
            setLoading(true);
            files.forEach((file, index) => {
                let formData = new FormData();
                formData.append("file", file);
                FileDataService.uploadFile(formData)
                    .then((response) => {
                        const item = response.data;
                        //console.log(item);
                        sendMessage(item, index);
                    })
                    .catch((e) => {
                        console.log(e.response);
                        setLoading(false);
                        inputSnackBarDetails("Something went wrong!","error");
                    });
            });
        }
    }

    const sendMessage = (fileResponse, index) => {
        let message = (fileType == "image") ? `[img=${fileResponse.url}]` :
            (fileType == "file") ?
                `[url=${fileResponse.url}]` : `[vid=${fileResponse.url}]`;
        if (groupId == undefined) {
            let messageRequest = {
                message,
                userId: Claims.getUserId(),
                individualChat: true,
                receiverId: userId
            }
            ChatDataService.sendMessage(messageRequest)
                .then((response) => {
                    const item = response.data;
                    if (item == "Message Saved") {
                        dispatch({
                            type: 'SET_MESSAGE',
                            message: messageRequest
                        });
                        if (index == files.length - 1) {
                            setLoading(false);
                            handelCancel();
                        }
                    }
                })
                .catch((e) => {
                    console.log(e.response);
                    setLoading(false);
                    inputSnackBarDetails("Something went wrong!","error");
                });
        }
        else {
            let messageRequest = {
                message,
                userId: Claims.getUserId(),
                groupId
            }
            GroupDataService.sendMessage(messageRequest)
                .then((response) => {
                    const item = response.data;
                    if (item == "Message Saved") {
                        dispatch({
                            type: 'SET_MESSAGE',
                            message: messageRequest
                        });
                        if (index == files.length - 1) {
                            setLoading(false);
                            handelCancel();
                        }
                    }
                })
                .catch((e) => {
                    console.log(e.response);
                    setLoading(false);
                    inputSnackBarDetails("Something went wrong!","error");
                });
        }
    }

    return (
        <div>
            <Dialog
                fullScreen={fullScreen}
                open={open}
                aria-labelledby="responsive-dialog-title"
            >
                <DialogTitle id="responsive-dialog-title">
                    {fileType == "image" ? "Send Image?" : fileType == "file" ? "Send File?" : "Send Video?"}
                </DialogTitle>
                <DialogContent>
                    <Paper elevation={5} sx={{ padding: "15px", textAlign: 'center' }}>
                        <section>
                            <div {...getRootProps({ className: 'dropzone' })}>
                                <input {...getInputProps()} />
                                {
                                    fileType == "image" ?
                                        <p>Drag 'n' drop some images here, or <b>click</b> to select images</p>
                                        : fileType == "file" ? <p>Drag 'n' drop some files here, or <b>click</b> to select files</p>
                                            : <p>Drag 'n' drop some video files here, or <b>click</b> to select video files</p>
                                }
                                <em>(2 files are the maximum number of files you can drop here)</em>
                                <br />
                                <br />
                            </div>
                            <Divider />
                            {
                                fileType == "image" ?
                                    <aside style={thumbsContainer}>
                                        {thumbs}
                                    </aside>
                                    :
                                    <List dense={false}>
                                        {
                                            files.map(file => (
                                                <ListItem key={file.path}>
                                                    <ListItemIcon>
                                                        <FilePresentIcon />
                                                    </ListItemIcon>
                                                    <ListItemText
                                                        primary={`${file.path} - ${file.size} bytes`}
                                                    />
                                                </ListItem>
                                            ))
                                        }
                                    </List>
                            }
                        </section>
                        {/* <TextField fullWidth label="Add a caption..." size="small"
                            value={caption} onChange={handleChange} /> */}
                    </Paper>
                </DialogContent>
                <DialogActions>
                    <LoadingButton loading={loading} variant="contained" onClick={handelCancel}>
                        Cancel
                    </LoadingButton>
                    <LoadingButton variant="contained" endIcon={<SendIcon />} onClick={handleFileSend} loading={loading} loadingPosition="end">
                        Send
                    </LoadingButton>
                </DialogActions>
            </Dialog>
        </div>
    );
}
