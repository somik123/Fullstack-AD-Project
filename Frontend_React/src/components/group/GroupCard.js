import { useNavigate } from 'react-router-dom';
// material
import { styled } from '@mui/material/styles';
import { Button, Card, Grid, Avatar, Typography, CardContent } from '@mui/material';

import SvgIconStyle from './SvgIconStyle';
// ----------------------------------------------------------------------

const CardMediaStyle = styled('div')({
  position: 'relative',
  paddingTop: 'calc(100% * 3 / 4)',
});

const DescStyle = styled('div')({
  height: 35,
  overflow: 'hidden',
  WebkitLineClamp: 2,
  display: '-webkit-box',
  WebkitBoxOrient: 'vertical',
  marginTop: 20,
  fontSize: 14
});

const AvatarStyle = styled(Avatar)(({ theme }) => ({
  zIndex: 9,
  width: 32,
  height: 32,
  position: 'absolute',
  left: theme.spacing(3),
  bottom: theme.spacing(-2),
}));

const InfoStyle = styled('div')(({ theme }) => ({
  display: 'flex',
  flexWrap: 'wrap',
  justifyContent: 'flex-end',
  marginTop: theme.spacing(3),
  color: theme.palette.text.disabled,
}));

const CoverImgStyle = styled('img')({
  top: 0,
  width: '100%',
  height: '100%',
  objectFit: 'cover',
  position: 'absolute',
});


export default function GroupCard({ group, tabDispatch }) {
  const { description, name, creatorName, id, creatorPhoto } = group;
  let navigate = useNavigate();

  const viewGroup = (groupId) => {
    tabDispatch({ type: 'SET_INDEX', index: 4 });
    navigate(`chat/${groupId}`)
  }

  return (
    <Grid item xs={12} sm={6} md={3}>
      <Card sx={{ position: 'relative' }}>
        <CardMediaStyle
        >
          <SvgIconStyle
            color="paper"
            src="/assets/shape-avatar.svg"
            sx={{
              width: 80,
              height: 36,
              zIndex: 9,
              bottom: -15,
              position: 'absolute',
              color: 'background.paper',
            }}
          />
          <AvatarStyle
            alt='profile photo'
            src={creatorPhoto}
          />

          <CoverImgStyle alt={name} src='/assets/covers/group_cover_3.jpg' />
        </CardMediaStyle>

        <CardContent
          sx={{
            pt: 4,
          }}
        >
          <Typography variant="caption" sx={{ color: 'brown', display: 'block' }}>
            Created by {creatorName}
          </Typography>

          <Typography sx={{ color: 'black', display: 'block' }}
            variant="subtitle1"
          >
            {name}
          </Typography>

          {/* <DescStyle>
            {description}
          </DescStyle> */}

          <InfoStyle>
            <Button variant="outlined" onClick={() => viewGroup(id)}>View Discussion</Button>
          </InfoStyle>
        </CardContent>
      </Card>
    </Grid>
  );
}
