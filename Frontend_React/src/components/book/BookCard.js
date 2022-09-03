// material
import { Box, Card, Link, Typography, Stack, Grid } from '@mui/material';
import { styled } from '@mui/material/styles';

const TitleStyle = styled('div')({
  height: 24,
  overflow: 'hidden',
  WebkitLineClamp: 1,
  display: '-webkit-box',
  WebkitBoxOrient: 'vertical',
  fontSize: '18px'
});

const DescStyle = styled('div')({
  height: 40,
  overflow: 'hidden',
  WebkitLineClamp: 2,
  display: '-webkit-box',
  WebkitBoxOrient: 'vertical',
  marginTop: 20,
});

const AuthorStyle = styled('div')({
  height: 20,
  color: 'brown',
  overflow: 'hidden',
  WebkitLineClamp: 1,
  display: '-webkit-box',
  WebkitBoxOrient: 'vertical',
});

export default function BookCard({ book }) {
  const { author, description, link, photo, title, genre, publishDate, id } = book;

  return (
    <Card>
      <Stack direction="row">
        <Box sx={{ pr: '20%', pt: '20%', position: 'relative' }}>
          <Grid
            item sx={{
              backgroundImage: `url(${photo})`,
              backgroundRepeat: 'no-repeat',
              backgroundColor: (t) => t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
              top: 0,
              width: '100%',
              height: '100%',
              objectFit: 'cover',
              position: 'absolute',
              backgroundPosition: 'center',
            }} />
        </Box>

        <Stack spacing={2} sx={{ p: 2, mt: 'auto', mb: 'auto' }}>
          <Link href={link} variant="h6" underline="hover" target="_blank">

            <TitleStyle>{title}</TitleStyle>

          </Link>

          <Stack direction="column" alignItems="start" justifyContent="space-between">
            <Typography variant="subtitle2">
              <AuthorStyle>By {author}</AuthorStyle>
            </Typography>
            <DescStyle>{description}</DescStyle>
          </Stack>
        </Stack>
      </Stack>

    </Card>
  );
}