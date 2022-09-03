import * as React from 'react';
import { Avatar, Skeleton } from "@mui/material";
import AccountDataService from '../../api/account-actions';

export default function ChatUserProfile(props) {

    const { userPhoto, userName, inputSnackBarDetails } = props
    const [user, setUser] = React.useState({});
    const [loading, setLoading] = React.useState(true);

    React.useEffect(() => {
        AccountDataService.getUserByuserName(userName)
            .then((response) => {
                const items = response.data;
                console.log(items);
                setUser(items);
                setLoading(false);
            })
            .catch((error) => {
                console.log(error);
                setLoading(false)
                inputSnackBarDetails("Something went wrong!", "error");
            });
    }, []);

    return (
        <>
            <div className="msg-profile group">
                {
                    loading ?
                        <Skeleton variant="circular" sx={{ backgroundColor: 'white' }} width={40} height={40} />
                        : <Avatar
                            src={userPhoto}
                        />
                }
            </div>
            <div className="msg-detail">
                {
                    loading ?
                        <Skeleton
                            animation="wave"
                            height="10px"
                            width="100px"
                            sx={{ backgroundColor: 'white' }}
                        />
                        : <div className="msg-username">{user.name} ({user.username})</div>
                }
                <div className="msg-content">
                    {
                        loading ?
                            <Skeleton
                                animation="wave"
                                height="10px"
                                width="100px"
                                sx={{ backgroundColor: 'white' }}
                            />
                            : <span className="msg-username">{user.email}</span>
                    }
                </div>
            </div>
        </>
    );
}
