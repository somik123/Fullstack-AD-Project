import React, { useEffect, useState } from 'react';
import { Divider, Avatar } from '@mui/material';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';
import Paper from '@mui/material/Paper';
import { useTheme } from '@mui/material/styles';
import { useDropzone } from 'react-dropzone';
import LoadingButton from '@mui/lab/LoadingButton';
import UploadIcon from '@mui/icons-material/Upload';

import FileDataService from '../../api/file-actions';
import AccountDataService from '../../api/account-actions';
import { Claims } from '../../utils/ClientCache'

const thumbsContainer = {
    display: 'flex',
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginTop: 16
};

export default function UploadImageDialog({ open, handleClose, dispatch, accountContext, inputSnackBarDetails }) {
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));
    const [files, setFiles] = useState([]);
    const [loading, setLoading] = React.useState(false);
    const { getRootProps, getInputProps } = useDropzone({
        maxFiles: 1,
        accept: {
            'image/*': []
        },
        onDrop: acceptedFiles => {
            setFiles(acceptedFiles.map(file => Object.assign(file, {
                preview: URL.createObjectURL(file)
            })));
        }
    });

    useEffect(() => {
        // Make sure to revoke the data uris to avoid memory leaks, will run on unmount
        return () => files.forEach(file => URL.revokeObjectURL(file.preview));
    }, []);

    const thumbs = files.map(file => (
        <Avatar key={file.name}
            src={file.preview}
            sx={{
                height: 100,
                mb: 2,
                width: 100,
                margin: 'auto'
            }}
        />
    ));

    const handelCancel = () => {
        setFiles([]);
        handleClose();
    }

    const handleFileSend = () => {
        if (files.length > 0) {
            setLoading(true);
            let formData = new FormData();
            formData.append("file", files[0]);
            FileDataService.uploadFile(formData)
                .then((response) => {
                    const item = response.data;
                    updateProfileImage(item);
                })
                .catch((e) => {
                    console.log(e.response);
                    setLoading(false);
                    alert("Something went wrong!");
                });
        }
    }

    const updateProfileImage = (fileResponse) => {
        let user = {
            id: accountContext.account.id,
            name: accountContext.account.name,
            email: accountContext.account.email,
            photo: fileResponse.url,
        }
        AccountDataService.updateUser(user)
            .then((_response) => {
                inputSnackBarDetails("Your profile image is succesfully updated!", 'success');
                accountContext.account.photo = fileResponse.url;
                dispatch({
                    type: 'SET_ACCOUNT',
                    account: accountContext.account
                });
                Claims.setProfileImage(fileResponse.url);
                setLoading(false);
                handelCancel();
            })
            .catch((e) => {
                console.log(e.response);
                setLoading(false);
                inputSnackBarDetails("Somethig went wrong!", 'error');
            });
    }

    return (
        <div>
            <Dialog
                fullScreen={fullScreen}
                open={open}
                aria-labelledby="responsive-dialog-title"
            >
                <DialogTitle id="responsive-dialog-title">
                    {"Upload Profile Image?"}
                </DialogTitle>
                <DialogContent>
                    <Paper elevation={5} sx={{ padding: "15px", textAlign: 'center' }}>
                        <section>
                            <div {...getRootProps({ className: 'dropzone' })}>
                                <input {...getInputProps()} />
                                <p>Drag 'n' drop profile image here, or <b>click</b> to select image</p>
                            </div>
                            <Divider />
                            <aside style={thumbsContainer}>
                                {thumbs}
                            </aside>
                        </section>
                    </Paper>
                </DialogContent>
                <DialogActions>
                    <LoadingButton loading={loading} variant="contained" onClick={handelCancel}>
                        Cancel
                    </LoadingButton>
                    <LoadingButton variant="contained" endIcon={<UploadIcon />}
                        onClick={handleFileSend} loading={loading} loadingPosition="end">
                        Upload
                    </LoadingButton>
                </DialogActions>
            </Dialog>
        </div>
    );
}
