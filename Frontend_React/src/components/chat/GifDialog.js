import React, { useEffect, useState } from 'react';
import { Grid, Gif } from '@giphy/react-components'
import { GiphyFetch } from '@giphy/js-fetch-api'
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import LoadingButton from '@mui/lab/LoadingButton';
import SendIcon from '@mui/icons-material/Send';

import GroupDataService from '../../api/group-actions';
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

// use @giphy/js-fetch-api to fetch gifs, instantiate with your api key
const gf = new GiphyFetch('VTYR48Qp4bU7ZJD8KmybzGwdey1cQaYD')

function SearchDemo({ onGifClick }) {
    const [searchTerm, setSearchTerm] = React.useState('greeting');
    const fetchGifs = (offset) => gf.search(searchTerm, { offset, limit: 10 });

    const handleQuery = (e) => {
        if (e.target.value.length > 0) {
            setSearchTerm(e.target.value)
        }
    }

    return (
        <>
            <div className="search-bar-dialog">
                <input onChange={handleQuery}
                    placeholder="Search gif..."
                />
            </div>
            <Grid
                onGifClick={onGifClick}
                fetchGifs={fetchGifs}
                width={800}
                columns={3}
                gutter={6}
                key={searchTerm}
            />
        </>
    );
}

export default function GifDialog({ open, handleClose, dispatch, groupId, inputSnackBarDetails }) {
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));
    const [loading, setLoading] = React.useState(false);
    const [modalGif, setModalGif] = useState();

    const handelCancel = () => {
        setLoading(false);
        setModalGif(undefined);
        handleClose();
    }

    const sendMessage = () => {
        console.log(modalGif.images.original.url)
        if (modalGif && modalGif.images.original.url.length > 0) {
            setLoading(true);
            let message = `[img=${modalGif.images.original.url}]`;
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
                        setLoading(false);
                        handelCancel();
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
        <div>
            <Dialog
                fullScreen={fullScreen}
                open={open}
                maxWidth={'lg'}
                aria-labelledby="responsive-dialog-title"
            >
                <DialogTitle id="responsive-dialog-title">
                    {"Send GIF"}
                </DialogTitle>
                <DialogContent>
                    <SearchDemo
                        onGifClick={(gif, e) => {
                            console.log("gif", gif);
                            e.preventDefault();
                            setModalGif(gif);
                        }}
                    />
                    {modalGif && (
                        <div
                            style={{
                                position: "fixed",
                                top: 0,
                                left: 0,
                                right: 0,
                                bottom: 0,
                                display: "flex",
                                justifyContent: "center",
                                alignItems: "center",
                                background: "rgba(0, 0, 0, .8)"
                            }}
                            onClick={(e) => {
                                e.preventDefault();
                                setModalGif(undefined);
                            }}
                        >
                            <Gif gif={modalGif} width={200} />
                        </div>
                    )}
                </DialogContent>
                <DialogActions>
                    <LoadingButton loading={loading} variant="contained" onClick={handelCancel}>
                        Cancel
                    </LoadingButton>
                    <LoadingButton onClick={sendMessage} variant="contained" endIcon={<SendIcon />} loading={loading} loadingPosition="end">
                        Send
                    </LoadingButton>
                </DialogActions>
            </Dialog>
        </div>
    );
}
